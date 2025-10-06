package com.semprejuntos.api.controllers;

import com.semprejuntos.api.entities.DeviceStatus;
import com.semprejuntos.api.services.DeviceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceStatusService deviceStatusService;

    @GetMapping("/{imei}/status")
    public DeviceStatus getDeviceStatus(@PathVariable String imei) {
        DeviceStatus status = deviceStatusService.getLatestStatusByImei(imei);
        if (status == null) {
            throw new RuntimeException("Dispositivo não encontrado: " + imei);
        }
        return status;
    }
}
