package com.example.topgoback.PasswordResetTokens.Service;

import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import com.example.topgoback.PasswordResetTokens.Repository.PasswordResetTokenRepository;
import com.example.topgoback.Reviews.DTO.*;
import com.example.topgoback.Reviews.Repository.ReviewRepository;
import com.example.topgoback.Users.DTO.UserRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordResetTokenService {
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;


    public void addOne(String token){
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpirationTime(LocalDateTime.now().plusDays(1));
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public PasswordResetToken findOne(String token){
        return passwordResetTokenRepository.findByToken(token);
    }
    public void deleteOne(String token){
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
