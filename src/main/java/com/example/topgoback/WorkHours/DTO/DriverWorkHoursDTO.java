package com.example.topgoback.WorkHours.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class DriverWorkHoursDTO {
    public PaginatedResponse totalCount;
    public List<WorkHoursDTO> results = new ArrayList<WorkHoursDTO>();

    public DriverWorkHoursDTO() {
    }
    public DriverWorkHoursDTO(Page<WorkHoursDTO> page) {
        totalCount = new PaginatedResponse();
        totalCount.setTotalCount((int) page.getTotalElements());
        this.results = page.getContent();
    }


    public PaginatedResponse getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public List<WorkHoursDTO> getResults() {
        return results;
    }

    public void setResults(List<WorkHoursDTO> results) {
        this.results = results;
    }
}
