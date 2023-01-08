package com.example.topgoback.Messages.Service;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Messages.DTO.SendMessageDTO;
import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Notes.DTO.UserNoteListDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.UserMessagesListDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public UserMessagesListDTO findBySenderOrReceiver(int userId, Pageable pageable){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!");
        }
        Page<Message> page = messageRepository.findBySenderOrReceiver(user.get(),user.get(),pageable);
        List<UserMessagesDTO> userMessagesListDTO = UserMessagesDTO.convertToUserMessagesListDTO(page.getContent());
        UserMessagesListDTO response = new UserMessagesListDTO(new PageImpl<>(userMessagesListDTO, pageable, page.getTotalElements()));
        return response;
    }


    public UserMessagesDTO addOne(int senderId,SendMessageDTO sendMessageDTO) {
        Message message = new Message();

        Optional<User> sender = userRepository.findById(senderId);
        if(sender.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!");
        }
        Optional<User> receiver = userRepository.findById(sendMessageDTO.getReceiverId());
        if(receiver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Receiver does not exist!");
        }

        message.setReceiver(receiver.get());
        message.setSender(sender.get());
        message.setType(sendMessageDTO.getType());
        message.setTimeOfSending(LocalDateTime.now());
        message.setMessage(sendMessageDTO.getMessage());

        if(sendMessageDTO.getType() == MessageType.RIDE) {
            Optional<Ride> ride = rideRepository.findById(sendMessageDTO.getRideId());

            if (ride.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ride does not exist!");
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
