package com.example.topgoback.Panic.DTO;

import com.example.topgoback.Tools.PaginatedResponse;

import java.util.List;

public class GetAllPanicDTO extends PaginatedResponse {

    private List<PanicDTO> results;

    public GetAllPanicDTO() {
    }

    public List<PanicDTO> getResults() {
        return results;
    }

    public void setResults(List<PanicDTO> results) {
        this.results = results;
    }
}
