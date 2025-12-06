package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.dto.UserUpdateRequest;
import com.sempremjuntos.api.entities.DeviceInfoDTO;
import com.sempremjuntos.api.services.UserDeviceService;
import com.sempremjuntos.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private UserService userService;

    /**
     * Endpoint antigo – lista dispositivos do usuário.
     * GET /api/users/{userId}/devices
     */
    @GetMapping("/{userId}/devices")
    public List<DeviceInfoDTO> getDevicesByUser(@PathVariable Integer userId) {
        return userDeviceService.getDevicesByUserId(userId);
    }

    /**
     * Endpoint novo – atualiza dados do usuário:
     * - full_name
     * - phone_number
     * - fcm_token
     *
     * Todos os campos do body são opcionais.
     * Se algum vier null, o valor atual é mantido (COALESCE no SQL).
     *
     * PATCH /api/users/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable("id") Integer userId,
            @RequestBody UserUpdateRequest request
    ) {
        userService.updateUser(
                userId,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getFcmToken()
        );

        return ResponseEntity.noContent().build();
    }
}
