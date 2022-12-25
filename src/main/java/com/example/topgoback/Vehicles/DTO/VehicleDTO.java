package com.example.topgoback.Vehicles.DTO;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Vehicles.Model.VehicleType;

public class VehicleDTO {

    public VehicleType vehicleType;

    public String model;
    public String licenseNumber;
    public GeoLocationDTO currentLocation;
    public int passengerSeats;
    public boolean babyTransport;
    public boolean petTransport;
    public int typeId;


    public VehicleDTO(VehicleType vehicleType, String model, String licenseNumber, GeoLocationDTO currentLocation, int passengerSeats, boolean babyTransport, boolean petTransport, int typeId) {
        this.vehicleType = vehicleType;
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.currentLocation = currentLocation;
        this.passengerSeats = passengerSeats;
        this.babyTransport = babyTransport;
        this.petTransport = petTransport;
        this.typeId = typeId;
    }

    public VehicleDTO() {
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
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
