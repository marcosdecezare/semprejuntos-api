package com.sempremjuntos.api.entities;

public class LoginResponse {
    private String token;
    private Integer id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
    }

    public String getToken() { return token; }
    public Integer getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
}
