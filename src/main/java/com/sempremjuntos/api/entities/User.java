package com.sempremjuntos.api.entities;

import java.time.LocalDateTime;

public class User {
    private Integer id;
    private String fullName;
    private String email;
    private String passwordHash;
    private String role;
    private LocalDateTime createdAt;
    private String phoneNumber; // novo campo

    public User(Integer id, String fullName, String email, String passwordHash,
                String role, LocalDateTime createdAt, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPhoneNumber() { return phoneNumber; }
}
