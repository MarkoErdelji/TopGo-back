package com.example.topgoback.Panic.Model;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
public class Panic {
    @Id
    @SequenceGenerator(name = "mySeqGenPanic", sequenceName = "mySeqGenPanic", initialValue = 7, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenPanic")
    private int id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Ride ride;
    private LocalDateTime time;
    private String reason;

    public Panic() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
