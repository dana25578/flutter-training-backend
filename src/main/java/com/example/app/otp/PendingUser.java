package com.example.app.otp;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name="pending_users")
public class PendingUser {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,unique=true,length=30)
    private String username;
    @Column(nullable=false,unique=true,length=100)
    private String email;
    @Column(name="password_hash",nullable=false,length=255)
    private String passwordHash;
    @Column(name="phone_number",nullable=false,length=20)
    private String phoneNumber;
    @Column(length=255)
    private String address;
    @Column(name="created_at",nullable=false)
    private LocalDateTime createdAt;
    public PendingUser(){}
    @PrePersist
    protected void onCreate(){
        if(createdAt==null) createdAt = LocalDateTime.now();
    }
    public Long getId(){return id;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username =username;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public String getAddress(){return address;}
    public void setAddress(String address){this.address=address;}
    public LocalDateTime getCreatedAt(){ return createdAt; }
}