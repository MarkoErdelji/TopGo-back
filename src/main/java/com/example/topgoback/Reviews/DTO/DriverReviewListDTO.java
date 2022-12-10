package com.example.topgoback.Reviews.DTO;

import com.example.topgoback.Tools.PaginatedResponse;

import java.util.List;

public class DriverReviewListDTO extends PaginatedResponse {
    List<CreateReviewResponseDTO> results;

    public DriverReviewListDTO() {
    }

    public List<CreateReviewResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<CreateReviewResponseDTO> results) {
        this.results = results;
    }
}

