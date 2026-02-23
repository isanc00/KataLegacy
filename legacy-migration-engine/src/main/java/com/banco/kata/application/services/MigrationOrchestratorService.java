package com.banco.kata.application.services;

import com.banco.kata.application.services.ports.MigrationStrategy;
import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationRequest;
import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationResponse;
import com.banco.kata.application.utils.MigrationResponseFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class MigrationOrchestratorService {

    private final List<MigrationStrategy> migrationStrategies;

    public MigrationResponse migrate(MigrationRequest request) {
        long startTime = System.currentTimeMillis();
        UUID correlationId = UUID.randomUUID();

        return migrationStrategies.stream()
                .filter(strategy -> strategy.supports(request.getSourceLanguage()))
                .findFirst()
                .map(strategy -> strategy.migrate(request, correlationId, startTime))
                .orElseGet(() -> MigrationResponseFactory.buildErrorResponse(
                        correlationId,
                        request.getTransactionCode(),
                        "No se encontró estrategia de migración para el lenguaje: "
                                + request.getSourceLanguage()));
    }

}