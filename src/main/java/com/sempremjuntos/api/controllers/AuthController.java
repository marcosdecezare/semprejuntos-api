package com.sempremjuntos.api.controllers;

import com.sempremjuntos.api.entities.LoginRequest;
import com.sempremjuntos.api.entities.LoginResponse;
import com.sempremjuntos.api.entities.User;
import com.sempremjuntos.api.security.JwtUtil;
import com.sempremjuntos.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> user = userService.findByEmail(req.getEmail());
        if (user.isEmpty() || !userService.verifyPassword(req.getPassword(), user.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.get().getId(), user.get().getEmail());
        return ResponseEntity.ok(new LoginResponse(token, user.get()));
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");

        userService.register(name, email, password);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User created successfully"));
    }
}
