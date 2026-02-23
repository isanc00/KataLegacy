package com.banco.kata.infrastructure.adapters.in.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MigrationRequest {

    @NotBlank(message = "El código de transacción es obligatorio para trazabilidad.")
    @Schema(description = "Código del programa o transacción legacy (ej. TRXD01)", example = "ACC-9921")
    private String transactionCode;

    @NotBlank(message = "Debe especificar el lenguaje de origen.")
    @Pattern(regexp = "^(COBOL|DELPHI|RPG)$", message = "Lenguaje no soportado. Use COBOL, DELPHI o RPG.")
    @Schema(description = "Lenguaje del código fuente a migrar", example = "COBOL")
    private String sourceLanguage;

    @NotBlank(message = "El código legacy no puede estar vacío. Rechazado por seguridad.")
    @Schema(description = "Fragmento de código a procesar", example = "IF AMOUNT > 0\n  DISPLAY \"VALID\"\nELSE\n  DISPLAY \"INVALID\"\nEND-IF")
    private String legacyCode;

    @NotBlank(message = "Debe especificar el lenguaje de destino.(Por el momento solo soporta java)")
    @Pattern(regexp = "^(JAVA|PYTHON|C#)$", message = "Lenguaje de destino no soportado. Use JAVA, PYTHON o C#.")
    @Schema(description = "Lenguaje de destino para la migración", example = "JAVA")
    private String targetLanguage = "JAVA"; 
    
}