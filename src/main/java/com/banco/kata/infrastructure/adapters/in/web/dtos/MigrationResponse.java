package com.banco.kata.infrastructure.adapters.in.web.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MigrationResponse {
    
    @Schema(description = "ID único de la ejecución para auditoría en Cloud Logging", example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0")
    private UUID correlationId;

    @Schema(description = "Código de la transacción legacy de origen", example = "TRXD01")
    private String transactionCode;

    @Schema(description = "Estado final de la traducción", example = "PARTIAL_SUCCESS")
    private MigrationStatus status;

    @Schema(description = "Tiempo total de análisis AST y generación (milisegundos)", example = "42")
    private Long executionTimeMs;

    @Schema(description = "El código Java o moderno generado")
    private String modernCode;

    @Schema(description = "Lista de reglas de transformación de negocio aplicadas")
    private List<String> appliedRules;

    @Schema(description = "Advertencias de seguridad o tipado que requieren revisión humana")
    private List<String> warnings;

    public enum MigrationStatus {
        SUCCESS, PARTIAL_SUCCESS, FAILED
    }
}