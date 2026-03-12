package com.example.app.auth;
import com.example.app.auth.dto.*;
import org.springframework.web.bind.annotation.*;
import com.example.app.otp.OtpService;
import com.example.app.otp.dto.VerifyOtpRequest;
import com.example.app.user.User;
import com.example.app.user.UserRepository;
import com.example.app.otp.PendingUserRepository;
import com.example.app.otp.PendingUser;
import com.example.app.auth.dto.LoginRequest;
import com.example.app.auth.dto.LoginResponse;
import com.example.app.auth.dto.RegisterRequest;
import org.springframework.transaction.annotation.Transactional;
import com.example.app.security.JwtService;
import com.example.app.security.PermissionRepository;
import java.util.List;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class AuthController {
    private final AuthService service;
    private final OtpService otpService;
    private final UserRepository userRepo;
    private final PendingUserRepository pendingRepo;
    private final JwtService jwtService;
    private final PermissionRepository permissionRepo;
    public AuthController(AuthService service,OtpService otpService,UserRepository userRepo,PendingUserRepository pendingRepo,JwtService jwtService,PermissionRepository permissionRepo){
        this.service= service;
        this.otpService=otpService;
        this.userRepo=userRepo;
        this.pendingRepo = pendingRepo;
        this.jwtService = jwtService;
        this.permissionRepo = permissionRepo;
    }
    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest req){
        return service.register(req);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req){
        return service.login(req);
    }
    @PostMapping("/verify-otp")
    @Transactional
    public LoginResponse verifyOtp(@RequestBody VerifyOtpRequest req){
        try {
            String email=req.getEmail()==null?"":req.getEmail().trim().toLowerCase();
            String code =req.getCode()==null?"" :req.getCode().trim();
            if (email.isEmpty()||code.isEmpty()){
                return new LoginResponse(false,"Email and OTP are required");
            }
            boolean ok=otpService.verifyEmailOtp(email,code);
            if (!ok) return new LoginResponse(false,"Invalid OTP");
            PendingUser pending=pendingRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("Pending registration not found"));
            User user=new User();
            user.setUsername(pending.getUsername());
            user.setEmail(pending.getEmail());
            user.setPasswordHash(pending.getPasswordHash());
            user.setPhoneNumber(pending.getPhoneNumber());
            user.setAddress(pending.getAddress());
            user.setEmailVerified(true);
            user.setEnabled(true);
            User saved=userRepo.save(user);
            saved.getPermissions().add(permissionRepo.findByName("ORDER_CREATE").orElseThrow(()->new RuntimeException("ORDER_CREATE not found")));
            saved.getPermissions().add(permissionRepo.findByName("ORDER_READ_OWN").orElseThrow(()->new RuntimeException("ORDER_READ_OWN not found")));
            saved.getPermissions().add(permissionRepo.findByName("USER_READ_SELF").orElseThrow(()->new RuntimeException("USER_READ_SELF not found")));
            saved.getPermissions().add(permissionRepo.findByName("USER_UPDATE_SELF").orElseThrow(()->new RuntimeException("USER_UPDATE_SELF not found")));
            saved.getPermissions().add(permissionRepo.findByName("CART_READ_SELF").orElseThrow(()->new RuntimeException("CART_READ_SELF not found")));
            saved.getPermissions().add(permissionRepo.findByName("CART_UPDATE_SELF").orElseThrow(()->new RuntimeException("CART_UPDATE_SELF not found")));
            saved.getPermissions().add(permissionRepo.findByName("WISHLIST_READ_SELF").orElseThrow(()->new RuntimeException("WISHLIST_READ_SELF not found")));
            saved.getPermissions().add(permissionRepo.findByName("WISHLIST_UPDATE_SELF").orElseThrow(()->new RuntimeException("WISHLIST_UPDATE_SELF not found")));
            saved=userRepo.save(saved);
            pendingRepo.deleteByEmail(email);
            LoginResponse resp=new LoginResponse(true,"Email verified successfully",saved.getId(),saved.getUsername(),saved.getEmail(),saved.getPhoneNumber(),saved.getAddress());
            List<String> perms=saved.getPermissions().stream().map(p->p.getName()).toList();
            resp.setToken(jwtService.generateToken(saved.getId(),saved.getEmail(),perms));
            return resp;
    }catch (RuntimeException e){
        return new LoginResponse(false,e.getMessage());
    }
}
    @PostMapping("/resend-otp")
    public LoginResponse resendOtp(@RequestBody VerifyOtpRequest req){
        String email = req.getEmail()==null ?"": req.getEmail().trim().toLowerCase();
        if (email.isEmpty()) return new LoginResponse(false,"Email is required");
        otpService.sendEmailOtp(email);
        return new LoginResponse(true,"OTP resent to your email.",true,email);
    }

}
