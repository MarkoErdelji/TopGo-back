package com.example.topgoback.AccountActivationToken.Repository;

import com.example.topgoback.AccountActivationToken.Model.AccountActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountActivationTokenRepository extends JpaRepository<AccountActivationToken,Integer> {
}
