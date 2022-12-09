package com.example.topgoback.Users.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.Model.Passenger;

import java.util.List;

public class PassengerListResponseDTO extends PaginatedResponse {
    private List<CreatePassengerResponseDTO> results;

    public PassengerListResponseDTO(List<CreatePassengerResponseDTO> passengers){
        this.setTotalCount(passengers.size());
        this.results = passengers;
    }
    public PassengerListResponseDTO (){};
    public List<CreatePassengerResponseDTO> getPassengers() {
        return results;
    }

    public void setPassengers(List<CreatePassengerResponseDTO> passengers) {
        this.results = passengers;
    }
}
