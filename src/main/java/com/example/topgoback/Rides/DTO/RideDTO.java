package com.example.topgoback.Rides.DTO;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.DTO.DepartureDestinationDTO;
import com.example.topgoback.RejectionLetters.DTO.UserRejectionLetterDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Passenger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RideDTO {
    public Integer id;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public float totalCost;

    public UserRef driver;

    public ArrayList<UserRef> passengers;

    public float estimatedTimeInMinutes;

    public VehicleName vehicleType;

    public boolean babyTransport;

    public boolean petTransport;

    public UserRejectionLetterDTO rejection;

    public List<RouteForCreateRideDTO> locations;

    public Status status;

    public RideDTO() {
    }

    public RideDTO(Ride r){
        this.setBabyTransport(r.isForBabies());
        this.setPetTransport(r.isForAnimals());

        this.setLocations(new ArrayList<RouteForCreateRideDTO>());
        this.getLocations().add(new RouteForCreateRideDTO(r.getRoute()));

        this.setStartTime(r.getStart());
        this.setStatus(r.getStatus());

        this.setVehicleType(VehicleName.valueOf(r.getDriver().getVehicle().getVehicleType().getVehicleName()));
        this.setTotalCost(r.getPrice());

        this.setEstimatedTimeInMinutes(DistanceCalculator.getEstimatedTimeInMinutes(60,r.getRoute().getLenght()));

        this.setEndTime(r.getEnd());
        this.setDriver(new UserRef(r.getDriver()));
        if(r.getRejectionLetter()!= null) {
            UserRejectionLetterDTO u = new UserRejectionLetterDTO();
            u.setReason(r.getRejectionLetter().getReason());
            u.setTimeOfRejection(r.getRejectionLetter().getTimeOfRejection());
            this.setRejection(u);
        }
        ArrayList<UserRef> userRefList = new ArrayList<>();
        for(Passenger p:r.getPassenger()){
            userRefList.add(new UserRef(p));
        }
        this.setPassengers(userRefList);

        this.setId(r.getId());
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

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }



    public float getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(float estimatedTimeInMinutes) {
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

    public VehicleName getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleName vehicleType) {
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

    public List<RouteForCreateRideDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<RouteForCreateRideDTO> locations) {
        this.locations = locations;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
