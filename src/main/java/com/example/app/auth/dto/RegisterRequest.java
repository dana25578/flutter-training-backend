package com.example.app.auth.dto;
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    public String getUsername(){return username;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public void setUsername(String username){this.username= username;}
    public void setEmail(String email){this.email= email;}
    public void setPassword(String password){this.password=password;}
    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public String getAddress(){return address;}
    public void setAdress(String address){this.address=address;}
}
