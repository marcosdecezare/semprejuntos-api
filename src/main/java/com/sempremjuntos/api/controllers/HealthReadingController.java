package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.HealthReadingDTO;
import com.sempremjuntos.api.services.HealthReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor as leituras de saúde dos dispositivos.
 * Atualmente retorna temperaturas corporais válidas.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/health")
public class HealthReadingController {

    @Autowired
    private HealthReadingService service;

    /**
     * Retorna a lista de leituras de temperatura corporal de um dispositivo.
     * O resultado é ordenado do mais recente para o mais antigo e ignora registros nulos.
     */
    @GetMapping("/{deviceId}/body-temperature")
    @Operation(
            summary = "Listar temperatura corporal válida do dispositivo",
            description = "Retorna todas as leituras válidas (não nulas) de temperatura corporal do dispositivo informado, ordenadas do mais recente para o mais antigo.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public List<HealthReadingDTO> getBodyTemperatureByDevice(@PathVariable Integer deviceId) {
        return service.getBodyTemperatureByDevice(deviceId);
    }
}
