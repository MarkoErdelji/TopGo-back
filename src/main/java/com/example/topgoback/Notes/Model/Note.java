package com.example.topgoback.Notes.Model;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Users.Model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = true)
    private User user;


    private LocalDateTime timeOfWriting;
    private String message;


    public Note(){}

    public LocalDateTime getTimeOfWriting() {
        return timeOfWriting;
    }

    public void setTimeOfWriting(LocalDateTime timeOfWriting) {
        this.timeOfWriting = timeOfWriting;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
