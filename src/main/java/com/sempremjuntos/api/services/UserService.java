package com.sempremjuntos.api.services;

import com.sempremjuntos.api.entities.User;
import com.sempremjuntos.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean verifyPassword(String rawPassword, String hash) {
        return passwordEncoder.matches(rawPassword, hash);
    }

    public void register(String fullName, String email, String password) {
        String hashed = passwordEncoder.encode(password);
        repository.save(fullName, email, hashed);
    }

    // NOVO MÉTODO GENÉRICO
    public void updateUser(Integer userId,
                           String fullName,
                           String phoneNumber,
                           String fcmToken) {
        repository.updateUser(userId, fullName, phoneNumber, fcmToken);
    }
}
