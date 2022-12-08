package com.example.topgoback.Messages.Service;

import com.example.topgoback.Messages.DTO.SendMessageDTO;
import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Users.DTO.UserMessagesListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public UserMessagesDTO addOne(SendMessageDTO sendMessageDTO) {
//        Message message = new Message();
        UserMessagesDTO userMessagesDTO = UserMessagesDTO.getMockupData();
        userMessagesDTO.setMessage(sendMessageDTO.getMessage());
        userMessagesDTO.setRideId(sendMessageDTO.getRideId());
        userMessagesDTO.setReceiverId(sendMessageDTO.getReceiverId());
        userMessagesDTO.setType(sendMessageDTO.getType().toString());
        userMessagesDTO.setId(123);
        return userMessagesDTO;
//
//        message.setMessage(sendMessageDTO.getMessage());
//        message.setType(sendMessageDTO.getType());
//        message.setReceiver(receiver);
//        message.setSender(sender);
//        message.setTimeOfSending(LocalDateTime.now());
//        message.setRideId(sendMessageDTO.getRideId());
        //return messageRepository.save(message);
    }


}
