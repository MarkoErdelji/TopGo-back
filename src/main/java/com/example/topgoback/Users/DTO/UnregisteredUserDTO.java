package com.example.topgoback.Users.DTO;

import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.DTO.DepartureDestinationDTO;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UnregisteredUserDTO {

    @Valid
    @NotNull(message = "is required!")
    private List<DepartureDestinationDTO> locations ;
    @NotBlank(message = "is required!")
    private String vehicleType;
    @NotNull(message = "is required!")
    private boolean babyTransport;
    @NotNull(message = "is required!")
    private boolean petTransport;

    public List<DepartureDestinationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<DepartureDestinationDTO> locations) {
        this.locations = locations;
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
}
