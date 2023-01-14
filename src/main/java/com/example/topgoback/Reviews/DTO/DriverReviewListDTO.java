package com.example.topgoback.Reviews.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class DriverReviewListDTO {

    PaginatedResponse totalCount;
    List<CreateReviewResponseDTO> results;

    public DriverReviewListDTO(){}
    public DriverReviewListDTO(Page<CreateReviewResponseDTO> page) {
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

    public List<CreateReviewResponseDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<CreateReviewResponseDTO> results) {
        this.results = results;
    }
}

