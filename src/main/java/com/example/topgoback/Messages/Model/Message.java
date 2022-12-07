package com.example.topgoback.Messages.Model;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Users.Model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne(optional = false,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id",referencedColumnName = "id",nullable = false)
    private User receiver;
    @ManyToOne(optional=false,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name="sender_id",referencedColumnName = "id",nullable = false)
    private User sender;

    @Column(nullable = true)
    private Integer rideId;
}
