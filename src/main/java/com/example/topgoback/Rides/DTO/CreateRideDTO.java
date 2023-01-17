package com.example.topgoback.Rides.DTO;

import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Users.DTO.RidePassengerDTO;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public class CreateRideDTO {
    @Valid
    @NotNull
    private List<RouteForCreateRideDTO> locations ;
    @Valid
    @NotNull
    private List<RidePassengerDTO> passengers;
    @NotNull(message = "is required!")
    private VehicleName vehicleType;
    @NotNull(message = "is required!")
    private Boolean babyTransport;
    @NotNull(message = "is required!")
    private Boolean petTransport;

    private LocalDateTime scheduledTime;

    public List<RouteForCreateRideDTO> getLocations() {
        return locations;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
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

    public VehicleName getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleName vehicleType) {
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
}
