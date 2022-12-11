package com.example.topgoback.Users.DTO;

import com.example.topgoback.Tools.PaginatedResponse;

import java.util.ArrayList;

public class PassengerListDTO {

    private PaginatedResponse totalCount;

    private ArrayList<PassengerListResponseDTO> results;

    public PassengerListDTO(){
        totalCount = new PaginatedResponse();
    }

    public Integer getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public ArrayList<PassengerListResponseDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<PassengerListResponseDTO> results) {
        this.results = results;
    }

}
