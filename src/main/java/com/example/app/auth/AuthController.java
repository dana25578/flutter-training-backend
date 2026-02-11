package com.example.app.auth;
import com.example.app.auth.dto.*;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service){
        this.service= service;
    }
    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest req){
        return service.register(req);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req){
        return service.login(req);
    }
}
