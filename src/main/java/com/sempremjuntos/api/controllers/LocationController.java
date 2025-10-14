package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.LocationDTO;
import com.sempremjuntos.api.services.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador responsável por retornar a localização atual de um dispositivo.
 * Usa dados de GPS prioritariamente e faz fallback automático via LBS (Mozilla API).
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/devices")
public class LocationController {

    @Autowired
    private LocationService service;

    @GetMapping("/{deviceId}/location")
    @Operation(
            summary = "Última localização unificada do dispositivo",
            description = """
            Retorna a última coordenada resolvida do dispositivo:
            - Prioriza GPS (latitude/longitude válidas)
            - Faz fallback via LBS usando a Mozilla Location Service
            - Retorna 204 No Content caso nenhuma localização válida seja encontrada
            """,
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<LocationDTO> getUnifiedLocation(@PathVariable Integer deviceId) {
        var result = service.getCurrentLocation(deviceId);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.noContent().build(); // 204 se nada válido
        }
    }
}
