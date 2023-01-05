package com.example.topgoback.RejectionLetters.Model;

import com.example.topgoback.RejectionLetters.DTO.RejectionLetterDTO;
import com.example.topgoback.Rides.Model.Ride;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class RejectionLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reason;
    private LocalDateTime timeOfRejection;
    @OneToOne(optional=false)
    @JoinColumn(name="ride_id", unique=false, nullable=false)
    private Ride ride;

    public RejectionLetter() {
    }



    public RejectionLetter(int id, String reason, LocalDateTime timeOfRejection, Ride ride) {
        this.id = id;
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
        this.ride = ride;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
