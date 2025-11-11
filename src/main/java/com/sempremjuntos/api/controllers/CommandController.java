package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.CommandRequestDTO;
import com.sempremjuntos.api.services.CommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API genérica para envio de comandos do servidor para o relógio.
 * Protegida por JWT (bearer), seguindo o padrão do projeto.
 */
@RestController
@RequestMapping("/api/commands")
@CrossOrigin(origins = "*")
public class CommandController {

    @Autowired
    private CommandService service;

    @PostMapping
    @Operation(
            summary = "Enviar comando para o relógio",
            description = "Insere um registro na tabela semprejuntos.command_buffer com status 'PENDING'.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<Void> sendCommand(@RequestBody CommandRequestDTO request) {
        // validação mínima
        if (request.getDeviceId() == null || request.getCommandType() == null || request.getCommandType().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        service.sendCommand(request.getDeviceId(), request.getCommandType());
        return ResponseEntity.ok().build();
    }
}
