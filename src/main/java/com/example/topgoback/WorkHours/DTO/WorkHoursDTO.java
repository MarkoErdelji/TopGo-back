package com.example.topgoback.WorkHours.DTO;

import java.time.LocalDateTime;

public class WorkHoursDTO {

    public int id;
    public LocalDateTime start;
    public LocalDateTime end;

    public WorkHoursDTO() {
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
