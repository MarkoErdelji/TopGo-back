package com.example.topgoback.WorkHours.Model;

import com.example.topgoback.Users.Model.Driver;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
public class WorkHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    @OneToOne
    @JoinColumn(name="driver_id", unique=false, nullable=true)
    private Driver driver;
    public WorkHours() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
