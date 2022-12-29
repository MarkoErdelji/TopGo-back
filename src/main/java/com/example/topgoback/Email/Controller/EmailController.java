package com.example.topgoback.Email.Controller;

import com.example.topgoback.Email.Model.Email;
import com.example.topgoback.Users.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/email")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {
    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private UserService userService;
    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> sendEmail(@RequestBody Email email) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            userService.loadUserByUsername(email.getTo());
        }
        catch (UsernameNotFoundException e){
            return new ResponseEntity<>("No such user",HttpStatus.NOT_FOUND);
        }
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(email.getMessage(),true);
        mailSender.send(message);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}