package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.AlarmDTO;
import com.sempremjuntos.api.services.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor os endpoints de alarmes dos dispositivos.
 * Requer autenticação via Bearer Token (JWT).
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    /**
     * Retorna todos os alarmes associados a um dispositivo.
     * O resultado é ordenado por data de disparo (mais recentes primeiro).
     */
    @GetMapping("/{deviceId}")
    @Operation(
            summary = "Listar alarmes do dispositivo",
            description = "Retorna todos os alarmes do dispositivo informado, ordenados por data mais recente.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public List<AlarmDTO> getAlarmsByDevice(@PathVariable Integer deviceId) {
        return alarmService.getAlarmsByDevice(deviceId);
    }

    /**
     * Remove todos os alarmes associados a um dispositivo específico.
     * Retorna HTTP 204 (No Content) em caso de sucesso.
     */
    @DeleteMapping("/{deviceId}")
    @Operation(
            summary = "Limpar alarmes do dispositivo",
            description = "Remove todos os alarmes do dispositivo informado.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<Void> clearAlarmsByDevice(@PathVariable Integer deviceId) {
        alarmService.clearAlarmsByDevice(deviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
