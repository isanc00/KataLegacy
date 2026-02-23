package com.banco.kata.infrastructure.adapters.out.antlr.strategies;

import java.util.UUID;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.springframework.stereotype.Component;

import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationResponse;
import com.banco.kata.infrastructure.adapters.out.antlr.CobolLexer;
import com.banco.kata.infrastructure.adapters.out.antlr.CobolParser;
import com.banco.kata.infrastructure.adapters.out.antlr.visitors.CobolMigrationVisitor;
import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationRequest;
import com.banco.kata.application.services.ports.MigrationStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class CobolMigrationService implements MigrationStrategy {

    @Override
    public boolean supports(String sourceLanguage) {
        return "COBOL".equalsIgnoreCase(sourceLanguage);
    }

    @Override
    public MigrationResponse migrate(MigrationRequest request, UUID correlationId, long startTime) {

        try {
            CharStream input = CharStreams.fromString(request.getLegacyCode());
            CobolLexer lexer = new CobolLexer(input);

            lexer.removeErrorListeners();

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CobolParser parser = new CobolParser(tokens);

            parser.removeErrorListeners(); 
            parser.setErrorHandler(new BailErrorStrategy());

            BaseErrorListener errorListener = new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                        int line, int charPositionInLine,
                        String msg, RecognitionException e) {
                    throw new ParseCancellationException(
                            "Error de sintaxis COBOL [Línea " + line + ", Columna " + charPositionInLine + "]: " + msg);
                }
            };

            lexer.addErrorListener(errorListener);
            parser.addErrorListener(errorListener);

            CobolParser.ProgramContext tree = parser.program();

            CobolMigrationVisitor visitor = new CobolMigrationVisitor();
            String modernCode = visitor.visit(tree);

            long executionTime = System.currentTimeMillis() - startTime;

            boolean hasWarnings = !visitor.getWarnings().isEmpty();
            MigrationResponse.MigrationStatus finalStatus = hasWarnings
                    ? MigrationResponse.MigrationStatus.PARTIAL_SUCCESS
                    : MigrationResponse.MigrationStatus.SUCCESS;

            log.info("[{}] Migración completada en {}ms con estado {}",
                    correlationId, executionTime, finalStatus);

            return MigrationResponse.builder()
                    .correlationId(correlationId)
                    .transactionCode(request.getTransactionCode())
                    .status(finalStatus)
                    .executionTimeMs(executionTime)
                    .modernCode(modernCode)
                    .appliedRules(visitor.getAppliedRules())
                    .warnings(visitor.getWarnings())
                    .build();

        } catch(ParseCancellationException e){
            log.error("[{}] Error de sintaxis COBOL: {}", correlationId, e.getMessage());
            return buildErrorResponse(correlationId, request, e.getMessage());
        } 
        catch (Exception e) {
            log.error("[{}] Error crítico parseando el AST: {}", correlationId, e.getMessage());
            return buildErrorResponse(correlationId, request, "Error en el motor de parsing: " + e.getMessage());
        }
    }

    private MigrationResponse buildErrorResponse(UUID correlationId, MigrationRequest request, String errorMsg) {
        return MigrationResponse.builder()
                .correlationId(correlationId)
                .transactionCode(request.getTransactionCode())
                .status(MigrationResponse.MigrationStatus.FAILED)
                .executionTimeMs(0L)
                .warnings(java.util.List.of(errorMsg))
                .build();
    }
}
