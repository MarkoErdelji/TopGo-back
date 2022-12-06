package com.example.topgoback.Users.DTO;

import com.example.topgoback.Rides.Model.Ride;

import java.util.ArrayList;

public class UserRidesListDTO {

    private int totalCount;

    private ArrayList<Ride> results;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<Ride> getResults() {
        return results;
    }

    public void setResults(ArrayList<Ride> results) {
        this.results = results;
    }
}
