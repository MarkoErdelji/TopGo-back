package com.example.topgoback.Panic.DTO;

import com.example.topgoback.Panic.Model.Panic;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PanicDTO {
    public Integer id;
    public UserListResponseDTO user;

    public UserRideDTO ride;
    public LocalDateTime time;
    public String reason;

    public PanicDTO(Integer id, UserListResponseDTO user, UserRideDTO userRideDTO, LocalDateTime time, String reason) {
        this.id = id;
        this.user = user;
        this.ride = userRideDTO;
        this.time = time;
        this.reason = reason;
    }
    public PanicDTO(){}

    public PanicDTO(Panic panic) {
        this.setId(panic.getId());
        this.setUser(new UserListResponseDTO(panic.getUser()));
        this.setRide(new UserRideDTO(panic.getRide()));
        this.setTime(panic.getTime());
        this.setReason(panic.getReason());


    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserListResponseDTO getUser() {
        return user;
    }

    public void setUser(UserListResponseDTO user) {
        this.user = user;
    }

    public UserRideDTO getRide() {
        return ride;
    }

    public void setRide(UserRideDTO ride) {
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
}
