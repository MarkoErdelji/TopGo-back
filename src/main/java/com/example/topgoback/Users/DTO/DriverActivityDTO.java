package com.example.topgoback.Users.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DriverActivityDTO {
    @JsonProperty("isActive")
    private boolean isActive;

    public DriverActivityDTO(boolean isActive) {
        this.isActive = isActive;
    }

    public DriverActivityDTO() {
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
