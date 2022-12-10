package com.example.topgoback.RejectionLetters.DTO;

import java.time.LocalDateTime;

public class RejectionLetterDTO {
    private String reason;
    private LocalDateTime timeOfRejection;

    public RejectionLetterDTO(String reason, LocalDateTime timeOfRejection) {
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
    }
}
