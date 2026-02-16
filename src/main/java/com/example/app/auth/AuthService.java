package com.example.app.auth;
import com.example.app.auth.dto.*;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    public AuthService(UserRepository repo, BCryptPasswordEncoder encoder){
        this.repo=repo;
        this.encoder=encoder;
    }
    public LoginResponse register(RegisterRequest req){
        if (repo.existsByEmail(req.getEmail())){
            return new LoginResponse(false,"Email already used");
        }
        if (repo.existsByUsername(req.getUsername())){
            return new LoginResponse(false,"Username already used");
        }
        if(req.getPhoneNumber()==null||req.getPhoneNumber().trim().isEmpty()){
            return new LoginResponse(false, "phone number is required");
        }
        User user= new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        user.setPhoneNumber(req.getPhoneNumber());
        user.setAddress(req.getAddress()==null?null:req.getAddress().trim());
        user.setEnabled(true);
        repo.save(user);
        return new LoginResponse(true,"Account created successfully", user.getId(),user.getUsername(),user.getEmail(),user.getPhoneNumber(),user.getAddress());
    }
    public LoginResponse login(LoginRequest req){
        var userOpt=repo.findByEmail(req.getEmail());
        if (userOpt.isEmpty()){
            return new LoginResponse(false,"Invalid email or password");
        }
        User user = userOpt.get();
        if (!encoder.matches(req.getPassword(), user.getPasswordHash())){
            return new LoginResponse(false,"Invalid email or password");
        }
        return new LoginResponse(true,"Login successful",user.getId(),user.getUsername(),user.getEmail(),user.getPhoneNumber(),user.getAddress());
    }
}
