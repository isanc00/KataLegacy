package com.banco.kata.application.utils;

import java.util.List;
import java.util.UUID;

import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationResponse;

public class MigrationResponseFactory {
    
    public static MigrationResponse buildErrorResponse(UUID correlationId, String transactionCode, String errorMsg) {
        return MigrationResponse.builder()
                .correlationId(correlationId)
                .transactionCode(transactionCode)
                .status(MigrationResponse.MigrationStatus.FAILED)
                .executionTimeMs(0L)
                .warnings(List.of(errorMsg))
                .build();
    }
}
