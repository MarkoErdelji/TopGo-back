package com.example.topgoback.Panic.DTO;

import com.example.topgoback.Tools.PaginatedResponse;

import java.util.List;

public class GetAllPanicDTO  {
    private PaginatedResponse totalCount;

    private List<PanicDTO> results;

    public GetAllPanicDTO() {
    }

    public List<PanicDTO> getResults() {
        return results;
    }

    public void setResults(List<PanicDTO> results) {
        this.results = results;
    }

    public PaginatedResponse getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(PaginatedResponse totalCount) {
        this.totalCount = totalCount;
    }
}

