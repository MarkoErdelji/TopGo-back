package com.example.topgoback.Messages.Model;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Users.Model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;

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

    @Column(nullable = true)
    private Integer rideId;

    @ManyToOne(optional = false)
    private User sender;
    @ManyToOne(optional = false)
    private User receiver;


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



    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Integer getRideId() {
        return rideId;
    }

}
