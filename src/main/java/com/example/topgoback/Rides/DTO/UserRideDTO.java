package com.example.topgoback.Rides.DTO;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.GeoLocations.DTO.DepartureDestinationDTO;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.RejectionLetters.DTO.RejectionLetterDTO;
import com.example.topgoback.RejectionLetters.DTO.UserRejectionLetterDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserRideDTO {
    public Integer id;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public float totalCost;

    public UserRef driver;

    public ArrayList<UserRef> passengers;

    public int estimatedTimeInMinutes;

    public String vehicleType;

    public boolean babyTransport;

    public boolean petTransport;

    public UserRejectionLetterDTO rejection;

    public Status status;

    ArrayList<DepartureDestinationDTO> locations;

    public UserRideDTO() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserRideDTO(Ride ride){
        this.setId(ride.getId());
        this.setStartTime(ride.getStart());
        this.setEndTime(ride.getEnd());
        this.setDriver(new UserRef(ride.getDriver()));
        ArrayList<UserRef> passengers = new ArrayList<UserRef>();
        for(Passenger p:ride.getPassenger()){
            passengers.add(new UserRef(p));
        }
        this.setPassengers(passengers);
        GeoLocationDTO start = new GeoLocationDTO(ride.getRoute().getStart());
        GeoLocationDTO end = new GeoLocationDTO(ride.getRoute().getFinish());
        DepartureDestinationDTO departureDestinationDTO = new DepartureDestinationDTO();
        departureDestinationDTO.setDeparture(start);
        departureDestinationDTO.setDestination(end);
        ArrayList<DepartureDestinationDTO> locations = new ArrayList<DepartureDestinationDTO>();
        locations.add(departureDestinationDTO);
        this.setEstimatedTimeInMinutes((int) DistanceCalculator.getEstimatedTimeInMinutes(45,ride.getRoute().getLenght()));
        this.setLocations(locations);
        this.setTotalCost(ride.getPrice());
        if(ride.getRejectionLetter() != null){
            this.setRejection(new UserRejectionLetterDTO(ride.getRejectionLetter()));
        }
        this.setPetTransport(ride.isForAnimals());
        this.setBabyTransport(ride.isForBabies());
        this.setVehicleType(ride.getVehicleName().toString());
        this.setStatus(ride.getStatus());
    }
    public static List<UserRideDTO> convertToUserRideDTO(List<Ride> rides){
        List<UserRideDTO> userRideDTOList = new ArrayList<UserRideDTO>();
        for(Ride r:rides){
            userRideDTOList.add(new UserRideDTO(r));
        }
        return userRideDTOList;
    }

    public static UserRideDTO getMockupData(){
        UserRideDTO userRideDTO = new UserRideDTO();
        userRideDTO.setId(123);
        String str = "2017-07-21T17:45:14Z";
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
        userRideDTO.setBabyTransport(true);
        userRideDTO.setPetTransport(true);
        userRideDTO.setVehicleType("STANDARDNO");
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


    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public int getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(int estimatedTimeInMinutes) {
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
