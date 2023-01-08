package com.example.topgoback.Users.DTO;

public class CreateNoteDTO {

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
