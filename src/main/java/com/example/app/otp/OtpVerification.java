package com.example.app.otp;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="otp_verifications")
public class OtpVerification {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(nullable =false,length =120)
    private String email;
    @Column(nullable = false,length =255)
    private String codeHash;
    @Column(nullable =false)
    private LocalDateTime expiresAt;
    @Column(nullable = false)
    private boolean used=false;
    @Column(nullable = false)
    private int attempts=0;
    public Long getId(){return id;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getCodeHash(){return codeHash;}
    public void setCodeHash(String codeHash){this.codeHash=codeHash;}
    public LocalDateTime getExpiresAt(){return expiresAt;}
    public void setExpiresAt(LocalDateTime expiresAt){this.expiresAt=expiresAt;}
    public boolean isUsed(){return used;}
    public void setUsed(boolean used){this.used=used;}
    public int getAttempts(){return attempts;}
    public void setAttempts(int attempts){this.attempts=attempts;}
}
