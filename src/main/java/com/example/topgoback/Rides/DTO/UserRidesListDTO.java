package com.example.topgoback.Rides.DTO;

import com.example.topgoback.Rides.DTO.UserRideDTO;

import java.util.ArrayList;

public class UserRidesListDTO {

    private int totalCount;

    private ArrayList<UserRideDTO> results;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<UserRideDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<UserRideDTO> results) {
        this.results = results;
    }
}
