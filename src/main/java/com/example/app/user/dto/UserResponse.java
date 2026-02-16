package com.example.app.user.dto;
import java.time.LocalDateTime;
public class UserResponse {
    public Long id;
    public String username;
    public String email;
    public String phoneNumber;
    public String address;
    public boolean enabled;
    public LocalDateTime createdAt;
    
}
