package com.example.topgoback.Reviews.DTO;

import com.example.topgoback.Tools.PaginatedResponse;

import java.util.List;

public class VehicleReviewListDTO extends PaginatedResponse {
    List<CreateReviewResponseDTO> results;

    public VehicleReviewListDTO() {
    }

    public List<CreateReviewResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<CreateReviewResponseDTO> results) {
        this.results = results;
    }
}
