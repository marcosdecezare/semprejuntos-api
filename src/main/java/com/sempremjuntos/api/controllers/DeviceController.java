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

    @GetMapping("/{imei}/status")
    public Optional<DeviceStatusDTO> getDeviceStatus(@PathVariable String imei) {
        return deviceStatusService.getLatestStatus(imei);
    }
}
