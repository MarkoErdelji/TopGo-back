package com.example.topgoback.Rides.DTO;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.RejectionLetters.DTO.RejectionLetterDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Users.DTO.RidePassengerDTO;

import java.time.LocalDateTime;
import java.util.List;

public class RideResponseDTO {

    private int id;
    private List<RouteForCreateRideDTO> locations ;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<RidePassengerDTO> passengers;
    private Integer estimatedTimeInMinutes;
    private String vehicleType;

    private RejectionLetterDTO rejectionLetterDTO;

    private boolean babyTransport;
    private boolean petTransport;

    private String status;

    public RideResponseDTO(CreateRideDTO ride){
        this.id = 123;
        this.locations = ride.getLocations();
        this.startTime = LocalDateTime.now();
        this.endTime = startTime.plusMinutes(5);
        this.passengers = ride.getPassengers();
        this.estimatedTimeInMinutes = 5;
        this.vehicleType = ride.getVehicleType();
        this.babyTransport = ride.getBabyTransport();;
        this.petTransport = ride.getPetTransport();
        this.rejectionLetterDTO = new RejectionLetterDTO( "Reason", LocalDateTime.now());
        this.status = "PENDING";

    }

    public boolean isBabyTransport() {
        return babyTransport;
    }
    public List<RouteForCreateRideDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<RouteForCreateRideDTO> locations) {
        this.locations = locations;
    }

    public List<RidePassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<RidePassengerDTO> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Boolean getBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public Boolean getPetTransport() {
        return petTransport;
    }

    public void setPetTransport(Boolean petTransport) {
        this.petTransport = petTransport;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
