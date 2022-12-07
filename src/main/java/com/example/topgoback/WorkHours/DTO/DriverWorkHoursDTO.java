package com.example.topgoback.WorkHours.DTO;

import java.util.ArrayList;
import java.util.List;

public class DriverWorkHoursDTO {
    public int totalCount;
    public List<WorkHoursDTO> results = new ArrayList<WorkHoursDTO>();

    public DriverWorkHoursDTO() {
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<WorkHoursDTO> getResults() {
        return results;
    }

    public void setResults(List<WorkHoursDTO> results) {
        this.results = results;
    }
}
