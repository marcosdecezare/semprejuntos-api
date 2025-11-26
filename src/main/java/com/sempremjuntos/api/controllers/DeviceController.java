package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.DeviceStatusDTO;
import com.sempremjuntos.api.entities.DeviceConfigDTO;
import com.sempremjuntos.api.services.DeviceStatusService;
import com.sempremjuntos.api.services.DeviceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sempremjuntos.api.entities.DeviceConfigUpdateRequest;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*")
public class DeviceController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    @Autowired
    private DeviceConfigService deviceConfigService;

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

    @GetMapping("/{deviceId}/config")
    @Operation(
            summary = "Configuração completa do dispositivo (tabela devices)",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<DeviceConfigDTO> getDeviceConfig(@PathVariable Integer deviceId) {
        return deviceConfigService.getConfig(deviceId)
                .map(ResponseEntity::ok)              // 200 + body
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }


    @PatchMapping("/{deviceId}/config")
    @Operation(
            summary = "Atualiza parcialmente a configuração do dispositivo (tabela devices)",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    public ResponseEntity<DeviceConfigDTO> updateDeviceConfig(
            @PathVariable Integer deviceId,
            @RequestBody DeviceConfigUpdateRequest request
    ) {
        return deviceConfigService.updateConfig(deviceId, request)
                .map(ResponseEntity::ok)              // 200 + body atualizado
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 se id não encontrado
    }


}
