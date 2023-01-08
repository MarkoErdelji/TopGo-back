package com.example.topgoback.Users.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class AllDriversDTO {
    private PaginatedResponse totalCount;

    private List<DriverInfoDTO> results;

    public AllDriversDTO() {
    }

    public AllDriversDTO(Page<DriverInfoDTO> page) {
        totalCount = new PaginatedResponse();
        totalCount.setTotalCount((int) page.getTotalElements());
        this.results = page.getContent();
    }

    public PaginatedResponse getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(PaginatedResponse totalCount) {
        this.totalCount = totalCount;
    }

    public List<DriverInfoDTO> getResults() {
        return results;
    }

    public void setResults(List<DriverInfoDTO> results) {
        this.results = results;
    }
}
