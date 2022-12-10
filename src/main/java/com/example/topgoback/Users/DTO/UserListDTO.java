package com.example.topgoback.Users.DTO;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.Model.User;

import java.util.ArrayList;

public class UserListDTO {

    private PaginatedResponse totalCount;

    private ArrayList<UserListResponseDTO> results;

    public UserListDTO(){
        totalCount = new PaginatedResponse();
    }

    public Integer getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public ArrayList<UserListResponseDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<UserListResponseDTO> results) {
        this.results = results;
    }
}
