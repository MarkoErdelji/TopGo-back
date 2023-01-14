package com.example.topgoback.Users.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PassengerListDTO {

    private PaginatedResponse totalCount;

    private List<PassengerListResponseDTO> results;

    public PassengerListDTO(Page<PassengerListResponseDTO> page){
        totalCount = new PaginatedResponse();
        totalCount.setTotalCount((int) page.getTotalElements());
        this.results = page.getContent();
    }

    public Integer getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public List<PassengerListResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<PassengerListResponseDTO> results) {
        this.results = results;
    }

}
