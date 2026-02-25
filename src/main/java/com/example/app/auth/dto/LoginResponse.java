package com.example.app.auth.dto;
public class LoginResponse {
    private boolean success;
    private String message;
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean requiresVerification;
    public LoginResponse(boolean success,String message){
        this.success =success;
        this.message=message;
        this.requiresVerification= false;

    }
    public LoginResponse(boolean success, String message, Long id, String username,String email,String phoneNumber,String address){
        this.success=success;
        this.message=message;
        this.id=id;
        this.username=username;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.address=address;
        this.requiresVerification= false;
    }
    public LoginResponse(boolean success, String message,boolean requiresVerification,String email){
        this.success =success;
        this.message =message;
        this.requiresVerification= requiresVerification;
        this.email =email;
    }
    public boolean isSuccess(){return success;}
    public String getMessage(){return message;}
    public Long getId(){return id;}
    public String getUsername(){return username;}
    public String getEmail(){return email;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getAddress(){return address;}
    public boolean isRequiresVerification(){return requiresVerification;}
}
