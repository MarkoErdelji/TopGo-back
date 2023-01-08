package com.example.topgoback.FavouriteRides.DTO;

import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.RejectionLetters.DTO.UserRejectionLetterDTO;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Passenger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FavouriteRideInfoDTO {
    private int id;
    private String favoriteName;
    private List<RouteForCreateRideDTO> locations;
    private List<UserRef> passengers;

    private String vehicleType;
    private boolean babyTransport;
    private boolean petTransport;

    public FavouriteRideInfoDTO(FavouriteRide favouriteRide) {
        this.id = favouriteRide.getId();
        this.setFavoriteName(favouriteRide.getFavoriteName());
        this.setBabyTransport(favouriteRide.isBabyTransport());
        this.setPetTransport(favouriteRide.isPetTransport());

        this.setLocations(new ArrayList<RouteForCreateRideDTO>());
        this.getLocations().add(new RouteForCreateRideDTO(favouriteRide.getRoute()));


        this.setVehicleType(favouriteRide.getVehicleType());

        ArrayList<UserRef> userRefList = new ArrayList<>();
        for(Passenger p:favouriteRide.getPassengers()){
            userRefList.add(new UserRef(p));
        }
        this.setPassengers(userRefList);

    }


    public String getFavoriteName() {
        return favoriteName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
