package com.example.topgoback.Messages.Service;

import com.example.topgoback.Messages.DTOS.SendMessageDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Users.DTO.CreateUserDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<Message> findBySenderIdOrReceiverId(int userId){
        if (messageRepository.findBySenderIdOrReceiverId(userId,userId).isEmpty()){
            return null;
        }
        else {
            return messageRepository.findBySenderIdOrReceiverId(userId,userId);
        }
    }


    public Message addOne(int senderId,SendMessageDTO sendMessageDTO) {
        Message message = new Message();
        message.setMessage(sendMessageDTO.getMessage());
        message.setType(sendMessageDTO.getType());
        message.setReceiverId(sendMessageDTO.getReceiverId());
        message.setSenderId(senderId);
        message.setTimeOfSending(LocalDateTime.now());
        message.setRideId(sendMessageDTO.getRideId());
        return messageRepository.save(message);
    }
}
