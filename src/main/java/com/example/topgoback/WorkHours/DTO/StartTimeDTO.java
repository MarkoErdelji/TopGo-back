package com.example.topgoback.WorkHours.DTO;

import java.time.LocalDateTime;

public class StartTimeDTO {
    private LocalDateTime start;

    public StartTimeDTO(LocalDateTime start) {
        this.start = start;
    }

    public StartTimeDTO() {
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }
}
