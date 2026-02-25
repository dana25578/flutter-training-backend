package com.example.app.otp;
import com.example.app.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
@Service
public class OtpService {
    private final OtpVerificationRepository otpRepo;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    private final SecureRandom random=new SecureRandom();
    private static final int OTP_LENGTH=6;
    private static final int EXP_MINUTES =5;
    private static final int MAX_ATTEMPTS =5;
    public OtpService(OtpVerificationRepository otpRepo,EmailService emailService){
        this.otpRepo=otpRepo;
        this.emailService=emailService;
    }
    public void sendEmailOtp(String email){
        email = email.trim().toLowerCase();
        otpRepo.markAllUsedByEmail(email);
        String code=generateCode();
        OtpVerification otp=new OtpVerification();
        otp.setEmail(email);
        otp.setCodeHash(encoder.encode(code));
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(EXP_MINUTES));
        otp.setUsed(false);
        otp.setAttempts(0);
        otpRepo.save(otp);
        String subject="Your verification code";
        String html =buildOtpEmailHtml(code);
        try{
            emailService.sendEmailHtml(email,subject,html);
        } catch (MessagingException e){
            throw new RuntimeException("Failed to send OTP email: "+e.getMessage());
        }
    }
    public boolean verifyEmailOtp(String email,String code){
        OtpVerification otp =otpRepo.findTopByEmailOrderByIdDesc(email).orElseThrow(() -> new RuntimeException("No OTP found for this email"));
        if (otp.isUsed()) throw new RuntimeException("otp already used. Please tap resend to get a new code.");
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("OTP expired");
        if (otp.getAttempts()>=MAX_ATTEMPTS) throw new RuntimeException("Too many attempts");
        otp.setAttempts(otp.getAttempts()+1);
        boolean ok=encoder.matches(code, otp.getCodeHash());
        if (!ok){
            otpRepo.save(otp);
            return false;
        }
        otp.setUsed(true);
        otpRepo.save(otp);
        return true;
    }
    private String generateCode(){
        int min=(int) Math.pow(10,OTP_LENGTH-1); 
        int max =(int) Math.pow(10,OTP_LENGTH)-1;
        return String.valueOf(min+random.nextInt(max-min+1));
    }
    private String buildOtpEmailHtml(String code){
        return """
        <div style="font-family:Arial,sans-serif;background:#f6f7fb;padding:20px;">
          <div style="max-width:520px;margin:auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 6px 20px rgba(0,0,0,0.08);">
            <div style="padding:18px 22px;background:#111827;color:#fff;">
              <div style="font-size:18px;font-weight:700;">Verify your account</div>
              <div style="opacity:0.9;font-size:13px;">This code expires in 5 minutes.</div>
            </div>
            <div style="padding:18px 22px;">
              <div style="color:#374151;margin-bottom:12px;">Use this verification code:</div>
              <div style="font-size:28px;font-weight:800;letter-spacing:6px;background:#f3f4f6;padding:14px;border-radius:12px;text-align:center;">
                %s
              </div>
            </div>
          </div>
        </div>
        """.formatted(code);
    }
}