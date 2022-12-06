package com.example.topgoback.Messages.Service;

import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Users.DTO.CreateUserDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAll() {
        List<Message> users =  messageRepository.findAll();
        if(users.isEmpty()){
            return null;}
        else{
            return users;
        }
    }

    public List<Message> findBySenderOrReceiver(User user){
        if (messageRepository.findBySenderOrReceiver(user,user).isEmpty()){
            return null;
        }
        else {
            return messageRepository.findBySenderOrReceiver(user,user);
        }
    }


}
