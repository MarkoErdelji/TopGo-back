package com.example.topgoback.Messages.Repository;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Users.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message,Integer> {
    List<Message> findBySenderIdOrReceiverId(int senderId,int receiverId);
}
