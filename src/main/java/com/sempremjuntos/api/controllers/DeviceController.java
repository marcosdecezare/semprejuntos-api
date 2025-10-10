package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.DeviceStatusDTO;
import com.sempremjuntos.api.services.DeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    // Agora busca pelo device_id
    @GetMapping("/{deviceId}/status")
    public Optional<DeviceStatusDTO> getDeviceStatus(@PathVariable Integer deviceId) {
        return deviceStatusService.getLatestStatus(deviceId);
    }
}
