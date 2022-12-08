package com.example.topgoback.Rides.DTO;

import com.example.topgoback.GeoLocations.DTO.DepartureDestinationDTO;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.RejectionLetters.DTOS.UserRejectionLetterDTO;
import com.example.topgoback.RejectionLetters.Model.RejectionLetter;
import com.example.topgoback.Users.DTO.UserRef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserRideDTO {
    public Integer id;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public Integer totalCost;

    public UserRef driver;

    public ArrayList<UserRef> passengers;

    public Integer estimatedTimeInMinutes;

    public String vehicleType;

    public boolean babyTransport;

    public boolean petTransport;

    public UserRejectionLetterDTO rejection;

    ArrayList<DepartureDestinationDTO> locations;

    public UserRideDTO() {
    }

    public static UserRideDTO getMockupData(){
        UserRideDTO userRideDTO = new UserRideDTO();
        userRideDTO.setId(123);
        String str = "2017-07-21T17:32:28Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        userRideDTO.setStartTime(dateTime);
        userRideDTO.setEndTime(dateTime);
        userRideDTO.setTotalCost(1235);
        userRideDTO.setDriver(UserRef.getMockupData());
        List<UserRef> passengerList = new ArrayList<>();
        passengerList.add(UserRef.getMockupData());
        userRideDTO.setPassengers((ArrayList<UserRef>) passengerList);
        userRideDTO.setEstimatedTimeInMinutes(5);
        userRideDTO.setRejection(UserRejectionLetterDTO.getMockupData());
        List<DepartureDestinationDTO> locations = new ArrayList<>();
        locations.add(DepartureDestinationDTO.getMockedData());
        userRideDTO.setLocations((ArrayList<DepartureDestinationDTO>) locations);

        return userRideDTO;


    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }



    public Integer getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(Integer estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public UserRef getDriver() {
        return driver;
    }

    public void setDriver(UserRef driver) {
        this.driver = driver;
    }

    public ArrayList<UserRef> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<UserRef> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public UserRejectionLetterDTO getRejection() {
        return rejection;
    }

    public void setRejection(UserRejectionLetterDTO rejection) {
        this.rejection = rejection;
    }

    public ArrayList<DepartureDestinationDTO> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<DepartureDestinationDTO> locations) {
        this.locations = locations;
    }
}
