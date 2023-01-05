package com.example.topgoback.AccountActivationToken.Repository;

import com.example.topgoback.AccountActivationToken.Model.AccountActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountActivationTokenRepository extends JpaRepository<AccountActivationToken,Integer> {
    Optional<AccountActivationToken> findOneByPassenger_Id(int id);
}
