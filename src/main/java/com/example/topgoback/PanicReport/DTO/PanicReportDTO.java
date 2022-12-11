package com.example.topgoback.PanicReport.DTO;

import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Users.DTO.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PanicReportDTO {
    public Integer id;
    public UserListResponseDTO user;

    public UserRideDTO ride;
    public LocalDateTime time;
    public String reason;

    public PanicReportDTO(Integer id, UserListResponseDTO user, UserRideDTO userRideDTO, LocalDateTime time, String reason) {
        this.id = id;
        this.user = user;
        this.ride = userRideDTO;
        this.time = time;
        this.reason = reason;
    }
    public PanicReportDTO(){}

    public static PanicReportDTO getMokap(){
        PanicReportDTO panicReportDTO = new PanicReportDTO();
        panicReportDTO.setId(10);
        panicReportDTO.setUser(UserListResponseDTO.getMockupData());
        panicReportDTO.setRide(UserRideDTO.getMockupData());
        String str = "2022-12-11T13:31:10Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        panicReportDTO.setTime(dateTime);
        panicReportDTO.setReason("Driver is drinking while he drives");

        return panicReportDTO;
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
