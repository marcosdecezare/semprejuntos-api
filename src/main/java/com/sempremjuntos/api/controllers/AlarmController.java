package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.AlarmDTO;
import com.sempremjuntos.api.services.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor os alertas (alarmes) dos dispositivos monitorados.
 * Requer autenticação via Bearer Token (JWT).
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    /**
     * Retorna todos os alarmes associados a um determinado dispositivo.
     * O resultado é ordenado pela data de disparo (triggeredAt) em ordem decrescente.
     *
     * Exemplo de chamada:
     * GET /api/alarms/3
     * Header: Authorization: Bearer <token>
     *
     * @param deviceId ID do dispositivo
     * @return Lista de alarmes mais recentes primeiro
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
}
