package com.example.topgoback.Payments.Model;

import com.example.topgoback.Enums.PaymentType;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private PaymentType paymentType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    private LocalDateTime date;
    private float amount;
    @OneToOne(optional=false, mappedBy="payment")
    private Ride ride;

    public Payment() {
    }

    public Payment(int id, PaymentType paymentType, LocalDateTime date, float amount, Ride ride) {
        this.id = id;
        this.paymentType = paymentType;
        this.date = date;
        this.amount = amount;
        this.ride = ride;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
