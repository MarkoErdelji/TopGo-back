package com.example.topgoback.Vehicles.DTO;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateVehicleDTO {
    @NotNull(message = "is required!")
    public String vehicleType;
    @NotNull(message = "is required!")
    public String model;
    @NotNull(message = "is required!")
    public String licenseNumber;
    @Valid
    @NotNull(message = "is required!")
    public GeoLocationDTO currentLocation;
    @NotNull(message = "is required!")
    public int passengerSeats;
    @NotNull(message = "is required!")
    public boolean babyTransport;
    @NotNull(message = "is required!")
    public boolean petTransport;


    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public GeoLocationDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoLocationDTO currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int getPassengerSeats() {
        return passengerSeats;
    }

    public void setPassengerSeats(int passengerSeats) {
        this.passengerSeats = passengerSeats;
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
}
