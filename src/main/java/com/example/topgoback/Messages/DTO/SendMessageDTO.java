package com.example.topgoback.Messages.DTO;

import com.example.topgoback.Enums.MessageType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class SendMessageDTO {

    @NotNull(message = "is required!")
    @Length(max=255,message= "cannot be longer than 255 characters!")
    private String message;

    @NotNull(message = "is required!")
    private MessageType type;

    @NotNull(message = "is required!")
    private Integer rideId;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }
}
