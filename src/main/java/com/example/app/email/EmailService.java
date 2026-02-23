package com.example.app.email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService{
    private final JavaMailSender mailSender;
    @Value("${store.owner.email}")
    private String ownerEmail;
    @Value("${store.mail.from}")
    private String fromEmail;
    public EmailService(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }
    public void sendOwnerNewOrderEmail(String subject,String body){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(ownerEmail);
        message.setFrom(fromEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}