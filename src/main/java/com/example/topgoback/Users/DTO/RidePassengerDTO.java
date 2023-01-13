package com.example.topgoback.Users.DTO;

import jakarta.validation.constraints.NotNull;

public class RidePassengerDTO {
   /* "id": 123,
            "email": "user@example.com",
            "type": "VOZAC"*/
    @NotNull(message = "is required!")
    private Integer id;
    @NotNull(message = "is required!")

    private String email;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
