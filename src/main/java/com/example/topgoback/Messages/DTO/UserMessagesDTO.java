package com.example.topgoback.Messages.DTO;

import com.example.topgoback.Enums.MessageType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserMessagesDTO {
    public Integer id;
    public LocalDateTime timeOfSending;
    public Integer senderId;
    public Integer receiverId;
    public String message;
    public MessageType type;
    public Integer rideId;


    public UserMessagesDTO() {
    }

    public static UserMessagesDTO getMockupData(){
        UserMessagesDTO userMessagesDTO = new UserMessagesDTO();

        userMessagesDTO.setId(10);
        userMessagesDTO.setSenderId(123);
        userMessagesDTO.setReceiverId(123);
        String str = "2017-07-21T17:32:28Z";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        userMessagesDTO.setTimeOfSending(dateTime);
        userMessagesDTO.setMessage("The driver is going on a longer route on purpoes");
        userMessagesDTO.setType("RIDE");
        userMessagesDTO.setRideId(123);

        return userMessagesDTO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(LocalDateTime timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = MessageType.valueOf(type);
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }
}
