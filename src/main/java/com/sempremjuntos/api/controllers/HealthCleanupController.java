package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.CleanupType;
import com.sempremjuntos.api.entities.HealthCleanupRequestDTO;
import com.sempremjuntos.api.services.HealthCleanupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API genérica para limpeza de registros de health_readings.
 * Protegida por JWT (bearerAuth).
 *
 * DE-PARA aceito no campo type:
 *  - HEART   → limpa heart_rate
 *  - SYS_DIA → limpa systolic_bp e diastolic_bp (juntos)
 *  - SPO     → limpa spo2
 *  - TEMP    → limpa body_temperature
 *  - STEP    → limpa step_count
 *  - ALL     → limpa todas as linhas do device_id
 */
@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthCleanupController {

    @Autowired
    private HealthCleanupService service;

    @PostMapping("/cleanup")
    @Operation(
            summary = "Limpar registros da tabela health_readings (de-para)",
            description = """
            Envie deviceId e type com um dos valores: HEART, SYS_DIA, SPO, TEMP, STEP, ALL.
            Ex.: {"deviceId": 1, "type": "SYS_DIA"} → remove todas as linhas com sistólica/diastólica preenchidas.
            """,
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<String> clean(@RequestBody HealthCleanupRequestDTO request) {
        if (request.getDeviceId() == null || request.getType() == null || request.getType().isBlank()) {
            return ResponseEntity.badRequest().body("deviceId e type são obrigatórios. Valores aceitos para type: HEART, SYS_DIA, SPO, TEMP, STEP, ALL.");
        }

        CleanupType type = CleanupType.from(request.getType());
        if (type == null) {
            return ResponseEntity.badRequest().body("Tipo inválido. Use: HEART, SYS_DIA, SPO, TEMP, STEP, ALL.");
        }

        try {
            int removed = service.clean(request.getDeviceId(), type);
            return ResponseEntity.ok("Registros removidos: " + removed);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao limpar registros: " + e.getMessage());
        }
    }
}
