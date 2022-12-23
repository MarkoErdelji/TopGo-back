package com.example.topgoback.PasswordResetTokens.Repository;

import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Integer> {

    PasswordResetToken findByToken(String token);
}
