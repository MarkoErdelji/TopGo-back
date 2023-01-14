package com.example.topgoback.WorkHours.DTO;

import com.example.topgoback.WorkHours.Model.WorkHours;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkHoursDTO {

    public int id;
    public LocalDateTime start;
    public LocalDateTime end;

    public WorkHoursDTO() {
    }
    public WorkHoursDTO(WorkHours workHours) {
        this.id = workHours.getId();
        this.start = workHours.getStartHours();
        this.end = workHours.getEndHours();
    }
    public static List<WorkHoursDTO> convertToWorkHourDTO(List<WorkHours> workHours){
        List<WorkHoursDTO> workHoursDTOS = new ArrayList<WorkHoursDTO>();
        for(WorkHours wr : workHours){
            workHoursDTOS.add(new WorkHoursDTO(wr));
        }
        return workHoursDTOS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
