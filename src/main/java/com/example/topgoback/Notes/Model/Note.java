package com.example.topgoback.Notes.Model;

import com.example.topgoback.Enums.MessageType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String message;


    public Note(){}

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
}
