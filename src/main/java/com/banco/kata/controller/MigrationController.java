package com.banco.kata.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.kata.application.services.MigrationOrchestratorService;
import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationRequest;
import com.banco.kata.infrastructure.adapters.in.web.dtos.MigrationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/migrations")
@Tag(name = "Migración de Código Legacy a Java", description = "Endpoints para la migración automatizada de código legacy a Java(evolutivo a mas lenguajes) utilizando el motor AST.")
public class MigrationController {

    private final MigrationOrchestratorService orchestratorService;

    @PostMapping("/translate")
    @Operation(
        summary = "Traducir código Legacy a Moderno",
        description = "Recibe un fragmento de código, evalúa el lenguaje de origen mediante el patrón Strategy y retorna el código modernizado junto con métricas de ejecución."
    )
    public ResponseEntity<MigrationResponse> migrateCode(@Valid @RequestBody MigrationRequest request) {
        
        MigrationResponse response = orchestratorService.migrate(request);
        
        return ResponseEntity.ok(response);
    }
}