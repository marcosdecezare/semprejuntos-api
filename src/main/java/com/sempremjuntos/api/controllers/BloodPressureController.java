package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.BloodPressureDTO;
import com.sempremjuntos.api.services.BloodPressureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor as leituras de pressão arterial dos dispositivos.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/health")
public class BloodPressureController {

    @Autowired
    private BloodPressureService service;

    /**
     * Retorna as leituras válidas de pressão arterial (sistólica e diastólica)
     * de um dispositivo, ordenadas do mais recente para o mais antigo.
     */
    @GetMapping("/{deviceId}/blood-pressure")
    @Operation(
            summary = "Listar pressão arterial do dispositivo",
            description = "Retorna todas as leituras válidas (não nulas) de pressão arterial do dispositivo informado, ordenadas do mais recente para o mais antigo.",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public List<BloodPressureDTO> getBloodPressureByDevice(@PathVariable Integer deviceId) {
        return service.getBloodPressureByDevice(deviceId);
    }
}
