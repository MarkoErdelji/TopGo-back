package com.example.topgoback.WorkHours.DTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class StartTimeDTO {
    @NotNull
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
