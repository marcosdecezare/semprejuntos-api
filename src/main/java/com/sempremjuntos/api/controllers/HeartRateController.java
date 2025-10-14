package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.HeartRateDTO;
import com.sempremjuntos.api.services.HeartRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor as leituras de batimentos cardíacos dos dispositivos.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/health")
public class HeartRateController {

    @Autowired
    private HeartRateService service;

    /**
     * Retorna as leituras de batimentos cardíacos de um dispositivo.
     * O resultado é ordenado do mais recente para o mais antigo e ignora registros nulos.
     */
    @GetMapping("/{deviceId}/heart-rate")
    @Operation(
            summary = "Listar batimentos cardíacos do dispositivo",
            description = "Retorna todas as leituras válidas (não nulas) de batimentos cardíacos do dispositivo informado, ordenadas do mais recente para o mais antigo.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public List<HeartRateDTO> getHeartRateByDevice(@PathVariable Integer deviceId) {
        return service.getHeartRateByDevice(deviceId);
    }
}
