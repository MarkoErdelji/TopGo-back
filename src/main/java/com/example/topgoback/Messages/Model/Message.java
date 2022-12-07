package com.example.topgoback.Messages.Model;

import com.example.topgoback.Enums.MessageType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timeOfSending;
    private MessageType type;

    private int receiverId;

    private int senderId;

    @Column(nullable = true)
    private Integer rideId;

    public Message(){}

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

    public LocalDateTime getTimeOfSending() {
        return timeOfSending;
    }

    public void setTimeOfSending(LocalDateTime timeOfSending) {
        this.timeOfSending = timeOfSending;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiver_id) {
        this.receiverId = receiver_id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int sender_id) {
        this.senderId = sender_id;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }
}
