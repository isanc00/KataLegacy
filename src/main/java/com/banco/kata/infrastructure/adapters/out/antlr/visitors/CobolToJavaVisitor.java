package com.banco.kata.infrastructure.adapters.out.antlr.visitors;

import com.banco.kata.infrastructure.adapters.out.antlr.CobolBaseVisitor;
import com.banco.kata.infrastructure.adapters.out.antlr.CobolParser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CobolToJavaVisitor extends CobolBaseVisitor<String> {

    private final Set<String> appliedRules = new LinkedHashSet<>();
    private final List<String> warnings = new ArrayList<>();

    @Override
    public String visitProgram(CobolParser.ProgramContext ctx) {
        appliedRules.add("REGLA 1: Estructuración de clase wrapper para código migrado.");
        StringBuilder javaCode = new StringBuilder();

        javaCode.append("// --- Código Migrado a Java (Spring Boot) ---\n");
        javaCode.append("public class MigratedLegacyService {\n");
        javaCode.append(
                "    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MigratedLegacyService.class);\n\n");
        javaCode.append("    public void executeLogic() {\n");

        for (CobolParser.StatementContext stmt : ctx.statement()) {
            javaCode.append(visit(stmt)).append("\n");
        }

        javaCode.append("    }\n");
        javaCode.append("}\n");

        return javaCode.toString();
    }

    @Override
    public String visitIfStatement(CobolParser.IfStatementContext ctx) {
        appliedRules.add("REGLA 2: Traducción de bloque condicional IF/ELSE legacy a sintaxis C-like.");
        appliedRules.add("REGLA 3: Mapeo de operadores lógicos relacionales.");

        if (ctx.condition().size() > 1) {
            appliedRules.add("REGLA 5: Normalización de estructura ELSE IF.");
        }

        StringBuilder ifBlock = new StringBuilder();

        String mainCondition = ctx.condition(0).getText().toLowerCase();
        ifBlock.append("        if (").append(mainCondition).append(") {\n");

        for (CobolParser.StatementContext stmt : ctx.thenBlock(0).statement()) {
            ifBlock.append("            ").append(visit(stmt)).append("\n");
        }
        ifBlock.append("        }");

        for (int i = 1; i < ctx.condition().size(); i++) {
            String elseIfCond = ctx.condition(i).getText().toLowerCase();
            ifBlock.append(" else if (").append(elseIfCond).append(") {\n");
            
            for (CobolParser.StatementContext stmt : ctx.thenBlock(i).statement()) {
                ifBlock.append("            ").append(visit(stmt)).append("\n");
            }
            ifBlock.append("        }");
        }

        if (ctx.elseBlock() != null) {
            ifBlock.append(" else {\n");
            for (CobolParser.StatementContext stmt : ctx.elseBlock().statement()) {
                ifBlock.append("            ").append(visit(stmt)).append("\n");
            }
            ifBlock.append("        }\n");
        } else {
            ifBlock.append("\n");
        }

        warnings.add("WARNING [Seguridad/Tipado]: Condiciones múltiples evaluadas. Validar precisión en Java.");
        return ifBlock.toString();
    }

    @Override
    public String visitDisplayStatement(CobolParser.DisplayStatementContext ctx) {
        appliedRules.add("REGLA 4: Reemplazo de salida estándar (DISPLAY) por Logger empresarial.");

        String textToPrint = ctx.STRING() != null ? ctx.STRING().getText() : ctx.ID().getText();

        return "            logger.info(" + textToPrint + ");";
    }

    public List<String> getAppliedRules() {
        return new ArrayList<>(appliedRules);
    }

    public List<String> getWarnings() {
        return warnings;
    }
}