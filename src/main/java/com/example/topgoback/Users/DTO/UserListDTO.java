package com.example.topgoback.Users.DTO;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.User;

import java.util.ArrayList;

public class UserListDTO {

    private int totalCount;

    private ArrayList<UserListResponseDTO> results;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<UserListResponseDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<UserListResponseDTO> results) {
        this.results = results;
    }
}
