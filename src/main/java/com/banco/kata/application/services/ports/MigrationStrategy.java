package com.banco.kata.application.services.ports;

import java.util.UUID;

import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationRequest;
import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationResponse;

public interface MigrationStrategy {

    public boolean supports(String sourceLanguage);
    public MigrationResponse migrate(MigrationRequest request, UUID correlationId, long startTime);
    
}
