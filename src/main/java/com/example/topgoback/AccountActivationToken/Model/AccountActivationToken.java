package com.example.topgoback.AccountActivationToken.Model;


import com.example.topgoback.Users.Model.Passenger;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AccountActivationToken {
    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    @Column(name="id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    @Column
    private LocalDateTime expirationTime;

    public AccountActivationToken() {
    }

    public AccountActivationToken(Integer id, Passenger passenger, LocalDateTime expirationTime) {
        this.id = id;
        this.passenger = passenger;
        this.expirationTime = expirationTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
