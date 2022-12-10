package com.example.topgoback.Users.DTO;

import java.util.List;

public class AllDriversDTO {
    private Integer totalCount;

    private List<DriverInfoDTO> results;

    public AllDriversDTO() {
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<DriverInfoDTO> getResults() {
        return results;
    }

    public void setResults(List<DriverInfoDTO> results) {
        this.results = results;
    }
}
