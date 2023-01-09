package com.example.topgoback.Users.DTO;

import java.util.List;

public class AllActiveDriversDTO {
    private int totalCount;
    private List<DriverInfoDTO> results;

    public AllActiveDriversDTO(int totalCount, List<DriverInfoDTO> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public AllActiveDriversDTO() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<DriverInfoDTO> getResults() {
        return results;
    }

    public void setResults(List<DriverInfoDTO> results) {
        this.results = results;
    }
}
