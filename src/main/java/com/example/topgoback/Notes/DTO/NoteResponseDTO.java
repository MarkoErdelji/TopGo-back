package com.example.topgoback.Notes.DTO;

import java.time.LocalDateTime;

public class NoteResponseDTO {
    private Integer id;

    private String message;

    private LocalDateTime timeOfPosting;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeOfPosting() {
        return timeOfPosting;
    }

    public void setTimeOfPosting(LocalDateTime timeOfPosting) {
        this.timeOfPosting = timeOfPosting;
    }
}
