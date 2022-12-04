package com.example.topgoback.Messages.Model;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Users.Model.User;
import jakarta.persistence.*;

@Entity

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    private MessageType type;
    @OneToOne(optional=false)
    @JoinColumn(name="receiver_id", unique=false, nullable=false)
    private User receiver;
    @OneToOne(optional=false)
    @JoinColumn(name="sender_id", unique=false, nullable=false)
    private User sender;
}
