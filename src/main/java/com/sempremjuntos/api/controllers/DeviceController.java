package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.DeviceStatusDTO;
import com.sempremjuntos.api.services.DeviceStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class DeviceController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    // Antes: public Optional<DeviceStatusDTO> getDeviceStatus(...)
    @GetMapping("/{deviceId}/status")
    @Operation(
            summary = "Status do dispositivo (bateria, sinal, etc.)",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<DeviceStatusDTO> getDeviceStatus(@PathVariable Integer deviceId) {
        return deviceStatusService.getLatestStatus(deviceId)
                .map(ResponseEntity::ok)              // 200 + body
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }
}
