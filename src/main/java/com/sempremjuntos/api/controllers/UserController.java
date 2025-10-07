package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.DeviceInfoDTO;
import com.sempremjuntos.api.services.UserDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserDeviceService userDeviceService;

    @GetMapping("/{userId}/devices")
    public List<DeviceInfoDTO> getDevicesByUser(@PathVariable Integer userId) {
        return userDeviceService.getDevicesByUserId(userId);
    }
}
