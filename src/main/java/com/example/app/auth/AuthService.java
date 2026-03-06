package com.example.app.auth;
import com.example.app.auth.dto.*;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.app.otp.OtpService;
import com.example.app.otp.PendingUserRepository;
import com.example.app.otp.PendingUser;
import com.example.app.auth.dto.LoginRequest;
import com.example.app.auth.dto.LoginResponse;
import com.example.app.auth.dto.RegisterRequest;
import com.example.app.security.JwtService;
import java.util.List;
@Service
public class AuthService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final OtpService otpService;
    private final PendingUserRepository pendingRepo;
    private final JwtService jwtService;
    public AuthService(UserRepository repo, BCryptPasswordEncoder encoder,OtpService otpService,PendingUserRepository pendingRepo,JwtService jwtService){
        this.repo=repo;
        this.encoder=encoder;
        this.otpService=otpService;
        this.pendingRepo=pendingRepo;
        this.jwtService = jwtService;
    }
    public LoginResponse register(RegisterRequest req){
        String email=req.getEmail()==null?"":req.getEmail().trim().toLowerCase();
        String username=req.getUsername()==null ?"":req.getUsername().trim();
        String phone=req.getPhoneNumber()==null? "":req.getPhoneNumber().trim();
        String password=req.getPassword()==null ? "":req.getPassword().trim();
        if (email.isEmpty()) return new LoginResponse(false,"Email is required");
        if (username.isEmpty()) return new LoginResponse(false,"Username is required");
        if (phone.isEmpty()) return new LoginResponse(false,"Phone number is required");
        if (password.isEmpty()) return new LoginResponse(false,"Password is required");
        if (repo.existsByEmail(email)){
            return new LoginResponse(false,"This email is already used. Please login.");
        };
        if (repo.existsByPhoneNumber(phone)){
            return new LoginResponse(false,"This phone number is already used");
        }
        if (pendingRepo.existsByEmail(email)){
            return new LoginResponse(false,"This email is pending verification. Please login to verify.");
        }
        if (pendingRepo.existsByPhoneNumber(phone)){
            return new LoginResponse(false,"This phone number is pending verification. Please login to verify");
        }
        PendingUser pending=new PendingUser();
        pending.setEmail(email);
        pending.setUsername(username);
        pending.setPhoneNumber(phone);
        pending.setAddress(req.getAddress()==null?null:req.getAddress().trim());
        pending.setPasswordHash(encoder.encode(password));
        pendingRepo.save(pending);
        otpService.sendEmailOtp(email);
        return new LoginResponse(true,"OTP sent. Please verify your email.",true,email);
    }
    public LoginResponse login(LoginRequest req){
        String email=req.getEmail() ==null ?"":req.getEmail().trim().toLowerCase();
        String password =req.getPassword()==null?"":req.getPassword();
        if (email.isEmpty()||password.isEmpty()){
            return new LoginResponse(false,"Email and password are required");
        }
        var userOpt =repo.findByEmail(email);
        if (userOpt.isPresent()){
            User user= userOpt.get();
            if (!encoder.matches(password,user.getPasswordHash()))
                return new LoginResponse(false,"Invalid email or password");
            if (!user.isEnabled()){
                otpService.sendEmailOtp(email);
                return new LoginResponse(false,"Please verify your email to activate your account.",true,email);
            }
            LoginResponse resp =new LoginResponse(true,"Login successful",user.getId(),user.getUsername(),user.getEmail(),user.getPhoneNumber(),user.getAddress());
            List<String> perms=user.getPermissions().stream().map(p->p.getName()).toList();
            resp.setToken(jwtService.generateToken(user.getId(),user.getEmail(),perms));
            return resp;
        }
        var pendingOpt=pendingRepo.findByEmail(email);
        if (pendingOpt.isPresent()){
            PendingUser pending=pendingOpt.get();
            if (!encoder.matches(password, pending.getPasswordHash())){
                return new LoginResponse(false,"Invalid email or password");
            }
            otpService.sendEmailOtp(email);
            return new LoginResponse(false,"Please verify your email to activate your account.",true,email);
        }
        return new LoginResponse(false,"Invalid email or password");
    }
    
}
