package com.example.topgoback.Users.DTO;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class CreateNoteDTO {

    @NotNull(message = "is required!")
    @Length(max=255,message= "cannot be longer than 255 characters!")
    String message;

    public CreateNoteDTO() {
    }

    public CreateNoteDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
