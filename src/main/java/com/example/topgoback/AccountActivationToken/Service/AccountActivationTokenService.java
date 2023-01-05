package com.example.topgoback.AccountActivationToken.Service;

import com.example.topgoback.AccountActivationToken.Model.AccountActivationToken;
import com.example.topgoback.AccountActivationToken.Repository.AccountActivationTokenRepository;
import com.example.topgoback.Users.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class AccountActivationTokenService {
    @Autowired
    private AccountActivationTokenRepository accountActivationTokenRepository;
    @Autowired
    private PassengerService passengerService;

    public void addOne(int id){
        AccountActivationToken accountActivationToken = new AccountActivationToken();
        accountActivationToken.setPassenger(passengerService.findById(id));
        accountActivationToken.setExpirationTime(LocalDateTime.now().plusHours(1));
        accountActivationTokenRepository.save(accountActivationToken);
    }

    public AccountActivationToken findAndActivate(int id){
        Optional<AccountActivationToken> accountActivationToken = accountActivationTokenRepository.findOneByPassenger_Id(id);
        if(accountActivationToken.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activation with entered id does not exist!");
        } else if (accountActivationToken.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            accountActivationTokenRepository.deleteById(id);
            passengerService.delete(accountActivationToken.get().getPassenger().getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activation expired. Register again!");
        }
        passengerService.activate(accountActivationToken.get().getPassenger().getId());
        return accountActivationToken.get();
    }
    public List<AccountActivationToken> getAll(){
        List<AccountActivationToken> accountActivationTokens = accountActivationTokenRepository.findAll();
        return accountActivationTokens;
    }

    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        List<AccountActivationToken> accountActivationTokens = getAll();
        for(AccountActivationToken accountActivationToken: accountActivationTokens){
            if(accountActivationToken.getExpirationTime().isBefore(LocalDateTime.now())){
                accountActivationTokenRepository.deleteById(accountActivationToken.getId());
                passengerService.delete(accountActivationToken.getPassenger().getId());
                System.out.println("Activation" + accountActivationToken.getId() + " Deleted");
            }
        }
    }
}









