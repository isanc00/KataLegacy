package com.banco.kata.infrastructure.adapters.out.antlr.visitors;

import com.banco.kata.infrastructure.adapters.out.antlr.CobolBaseVisitor;
import com.banco.kata.infrastructure.adapters.out.antlr.CobolParser;

import java.util.ArrayList;
import java.util.List;

public class CobolMigrationVisitor extends CobolBaseVisitor<String> {

    private final List<String> appliedRules = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();

    @Override
    public String visitProgram(CobolParser.ProgramContext ctx) {
        appliedRules.add("REGLA 1: Estructuración de clase wrapper para código migrado.");
        StringBuilder javaCode = new StringBuilder();
        
        javaCode.append("// --- Código Migrado a Java (Spring Boot) ---\n");
        javaCode.append("public class MigratedLegacyService {\n");
        javaCode.append("    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MigratedLegacyService.class);\n\n");
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
        
        StringBuilder ifBlock = new StringBuilder();
        
        String condition = ctx.condition().getText().toLowerCase();
        ifBlock.append("        if (").append(condition).append(") {\n");

        for (CobolParser.StatementContext stmt : ctx.statement()) {
            if (stmt.getParent() == ctx && ctx.ELSE() == null) {
                ifBlock.append("    ").append(visit(stmt)).append("\n");
            }
        }
        
        if (ctx.ELSE() != null) {
            ifBlock.append("        } else {\n");
        }
        
        ifBlock.append("        }");
        
        warnings.add("WARNING [Seguridad/Tipado]: Condición '" + condition + "' evaluada estáticamente. Asegurar que las variables estén fuertemente tipadas en Java.");
        return ifBlock.toString();
    }

    @Override
    public String visitDisplayStatement(CobolParser.DisplayStatementContext ctx) {
        appliedRules.add("REGLA 4: Reemplazo de salida estándar (DISPLAY) por Logger empresarial.");
        
        String textToPrint = ctx.STRING() != null ? ctx.STRING().getText() : ctx.ID().getText();
        
        return "            logger.info(" + textToPrint + ");";
    }

    public List<String> getAppliedRules() {
        return appliedRules;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}