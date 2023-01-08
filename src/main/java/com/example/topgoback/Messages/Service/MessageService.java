package com.example.topgoback.Messages.Service;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Messages.DTO.SendMessageDTO;
import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.UserMessagesListDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;

    private JwtTokenUtil jwtTokenUtil;

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


    public UserMessagesDTO addOne(int senderId,SendMessageDTO sendMessageDTO) {
        Message message = new Message();

        Optional<User> sender = userRepository.findById(senderId);
        if(sender.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not exist!");
        }
        Optional<User> receiver = userRepository.findById(sendMessageDTO.getReceiverId());
        if(receiver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Receiver does not exist!");
        }

        message.setReceiver(receiver.get());
        message.setSender(sender.get());
        message.setType(sendMessageDTO.getType());
        message.setTimeOfSending(LocalDateTime.now());
        message.setMessage(sendMessageDTO.getMessage());

        if(sendMessageDTO.getType() == MessageType.RIDE) {
            Optional<Ride> ride = rideRepository.findById(sendMessageDTO.getRideId());

            if (ride.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ride does not exist!");
            }

            message.setRideId(ride.get().getId());
        }
        else{
            message.setRideId(null);
        }
        messageRepository.save(message);
        return new UserMessagesDTO(message);

    }


}
