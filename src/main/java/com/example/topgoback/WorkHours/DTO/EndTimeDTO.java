package com.example.topgoback.WorkHours.DTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EndTimeDTO {
    @NotNull
    private LocalDateTime end;

    public EndTimeDTO(LocalDateTime end) {
        this.end = end;
    }

    public EndTimeDTO() {
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
