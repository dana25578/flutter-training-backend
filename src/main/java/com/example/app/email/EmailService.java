package com.example.app.email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${store.owner.email}")
    private String ownerEmail;
    @Value("${store.mail.from}")
    private String fromEmail;
    public EmailService(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }
    public void sendOwnerNewOrderEmailHtml(String subject,String html) throws MessagingException{
        sendEmailHtml(ownerEmail,subject,html);
    }
    public void sendEmailHtml(String to,String subject,String html) throws MessagingException{
        MimeMessage message =mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,"UTF-8");
        helper.setTo(to);
        helper.setFrom(fromEmail);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }
}