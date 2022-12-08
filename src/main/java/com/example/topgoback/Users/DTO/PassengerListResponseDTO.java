package com.example.topgoback.Users.DTO;

import com.example.topgoback.Users.Model.Passenger;

import java.util.List;

public class PassengerListResponseDTO {
    private Integer count;
    private List<Passenger> passengers;

    public PassengerListResponseDTO(List<Passenger> passengers){
        this.count = passengers.size();
        this.passengers = passengers;
    }

    public PassengerListResponseDTO(Integer count, List<Passenger> passengers) {
        this.count = count;
        this.passengers = passengers;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
