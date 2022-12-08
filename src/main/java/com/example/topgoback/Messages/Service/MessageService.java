package com.example.topgoback.Messages.Service;

import com.example.topgoback.Messages.DTOS.SendMessageDTO;
import com.example.topgoback.Messages.DTOS.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Users.DTO.UserMessagesListDTO;
import com.example.topgoback.Users.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public UserMessagesListDTO findBySenderOrReceiver(int user){
        UserMessagesListDTO userMessagesListDTO = new UserMessagesListDTO();
        userMessagesListDTO.setTotalCount(243);
        ArrayList<UserMessagesDTO> userMessages = new ArrayList<>();
        userMessages.add(UserMessagesDTO.getMockupData());
        userMessagesListDTO.setResults(userMessages);
        return userMessagesListDTO;
//        if (messageRepository.findBySenderOrReceiver(user,user).isEmpty()){
//            return null;
//        }
//        else {
//            return messageRepository.findBySenderOrReceiver(user,user);
//        }
    }


    public Message addOne(User sender,User receiver,SendMessageDTO sendMessageDTO) {
        Message message = new Message();
        message.setMessage(sendMessageDTO.getMessage());
        message.setType(sendMessageDTO.getType());
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setTimeOfSending(LocalDateTime.now());
        message.setRideId(sendMessageDTO.getRideId());
        return messageRepository.save(message);
    }


}
