package com.example.topgoback.WorkHours.Model;

import com.example.topgoback.Users.Model.Driver;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
public class WorkHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime startHours;
    private LocalDateTime endHours;
    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartHours() {
        return startHours;
    }

    public void setStartHours(LocalDateTime startHours) {
        this.startHours = startHours;
    }

    public LocalDateTime getEndHours() {
        return endHours;
    }

    public void setEndHours(LocalDateTime endHours) {
        this.endHours = endHours;
    }

    public long getDifferenceInSeconds(){
        return this.endHours.minusSeconds(this.startHours.toEpochSecond(ZoneOffset.UTC)).toEpochSecond(ZoneOffset.UTC);
    }
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}