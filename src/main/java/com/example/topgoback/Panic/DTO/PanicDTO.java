package com.example.topgoback.Panic.DTO;

import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;

import java.time.LocalDateTime;

public class PanicDTO {
    public int id;
    public UserListResponseDTO user;
    public UserRideDTO ride;
    public LocalDateTime time;
    public String reason;

    public PanicDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
