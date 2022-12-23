package com.example.topgoback.PasswordResetTokens.Service;

import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import com.example.topgoback.PasswordResetTokens.Repository.PasswordResetTokenRepository;
import com.example.topgoback.Reviews.DTO.*;
import com.example.topgoback.Reviews.Repository.ReviewRepository;
import com.example.topgoback.Users.DTO.UserRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.security.auth.login.CredentialExpiredException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public PasswordResetToken findOne(String token) throws Exception {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository.findOneByToken(token);
        if(passwordResetTokenOptional.isPresent()){
            PasswordResetToken passwordResetToken = passwordResetTokenOptional.get();
            if(passwordResetToken.getExpirationTime().isBefore(LocalDateTime.now().minusDays(1))){
                throw new CredentialExpiredException();
            }
            else {
                return passwordResetTokenOptional.get();
            }
        }
        else{
            throw new Exception();
        }
    }
    public void deleteOne(String token) throws Exception {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository.findOneByToken(token);
        if(passwordResetTokenOptional.isPresent()){
            passwordResetTokenRepository.delete(passwordResetTokenOptional.get());
        }
        else{
            throw new Exception();
        }
    }
}
