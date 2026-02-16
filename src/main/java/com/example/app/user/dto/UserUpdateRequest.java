package com.example.app.user.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
public class UserUpdateRequest {
    @Size(min=3,max =30)
    public String username;
    @Email
    public String email;
    public Boolean enabled;
    public String phoneNumber;
    public String address;
    
}
