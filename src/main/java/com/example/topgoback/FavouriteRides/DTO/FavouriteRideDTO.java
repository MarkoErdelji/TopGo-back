package com.example.topgoback.FavouriteRides.DTO;

import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Users.DTO.UserRef;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FavouriteRideDTO {

    @NotNull
    private String favoriteName;
    @Valid
    @NotNull
    private List<RouteForCreateRideDTO> locations;
    @Valid
    @NotNull
    private List<UserRef> passengers;

    @NotNull
    private String vehicleType;

    @NotNull
    private boolean babyTransport;

    @NotNull
    private boolean petTransport;

    public FavouriteRideDTO() {
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public List<RouteForCreateRideDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<RouteForCreateRideDTO> locations) {
        this.locations = locations;
    }

    public List<UserRef> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserRef> passengers) {
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
}
