package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.OxygenationDTO;
import com.sempremjuntos.api.services.OxygenationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor as leituras de oxigenação (SpO₂) dos dispositivos.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/health")
public class OxygenationController {

    @Autowired
    private OxygenationService service;

    /**
     * Retorna as leituras de oxigenação (SpO₂) de um dispositivo.
     * O resultado é ordenado do mais recente para o mais antigo e ignora registros nulos.
     */
    @GetMapping("/{deviceId}/oxygenation")
    @Operation(
            summary = "Listar oxigenação (SpO₂) do dispositivo",
            description = "Retorna todas as leituras válidas (não nulas) de oxigenação (SpO₂) do dispositivo informado, ordenadas do mais recente para o mais antigo.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public List<OxygenationDTO> getOxygenationByDevice(@PathVariable Integer deviceId) {
        return service.getOxygenationByDevice(deviceId);
    }
}
