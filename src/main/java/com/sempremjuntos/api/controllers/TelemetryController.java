package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.TelemetryDTO;
import com.sempremjuntos.api.services.TelemetryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @GetMapping("/{deviceId}/telemetry")
    @Operation(
            summary = "Últimos registros de telemetria do dispositivo",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<List<TelemetryDTO>> getLatestTelemetry(
            @PathVariable Integer deviceId,
            @RequestParam(defaultValue = "50") Integer limit
    ) {
        List<TelemetryDTO> data = telemetryService.getLatestTelemetry(deviceId, limit);
        return ResponseEntity.ok(data);
    }
}
