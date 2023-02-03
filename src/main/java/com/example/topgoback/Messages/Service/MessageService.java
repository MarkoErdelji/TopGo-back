package com.example.topgoback.Messages.Service;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.Messages.DTO.SendMessageDTO;
import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Repository.MessageRepository;
import com.example.topgoback.Notes.DTO.UserNoteListDTO;
import com.example.topgoback.Rides.Controller.CreateRideHandler;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.DTO.UserMessagesListDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Handlers.UserMessageHandler;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideRepository rideRepository;


    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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


    public UserMessagesDTO addOne(int receiverId,SendMessageDTO sendMessageDTO,String authorization) {
        Message message = new Message();

        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int id = jwtTokenUtil.getUserIdFromToken(jwtToken);
        Optional<User> sender = userRepository.findById(id);
        if(sender.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Sender does not exist!");
        }

        Optional<User> receiver = userRepository.findById(receiverId);
        if(receiver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Receiver does not exist!");
        }

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

        message.setReceiver(receiver.get());
        message.setSender(sender.get());
        message.setType(sendMessageDTO.getType());
        message.setTimeOfSending(LocalDateTime.now());
        message.setMessage(sendMessageDTO.getMessage());

        messageRepository.save(message);
        UserMessagesDTO msg = new UserMessagesDTO(message);
        sendUserMessageBoth(msg);
        return  msg;

    }




    public UserMessagesListDTO findBySenderandReceiver(Integer userId,String authorization) {
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int id = jwtTokenUtil.getUserIdFromToken(jwtToken);
        Optional<User> sender = userRepository.findById(id);
        if(sender.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Sender does not exist!");
        }
        Optional<User> receiver = userRepository.findById(userId);
        if(receiver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Receiver does not exist!");
        }
        List<Message> list1 = messageRepository.findBySenderAndReceiver(sender.get(),receiver.get());
        List<Message> list2 = messageRepository.findBySenderAndReceiver(receiver.get(),sender.get());

        list1.addAll(list2);

        list1.sort(Comparator.comparing(Message::getTimeOfSending));

        List<UserMessagesDTO> userMessagesListDTO = new ArrayList<>();
        for (Message m:list1)
        {
            userMessagesListDTO.add(new UserMessagesDTO(m));
        }

        UserMessagesListDTO response = new UserMessagesListDTO();
        response.setResults(userMessagesListDTO);

        response.setTotalCount(new PaginatedResponse(userMessagesListDTO.size()));

        return response;
    }

    public void sendUserMessageBoth(UserMessagesDTO update){
        List<WebSocketSession> sessions = new ArrayList<>();
            WebSocketSession webSocketSession = UserMessageHandler.userSession.get(update.receiverId.toString());
            WebSocketSession webSocketSession2 = UserMessageHandler.userSession.get(update.senderId.toString());
            if(webSocketSession != null){
                sessions.add(webSocketSession);
            }
            if(webSocketSession2 != null){
                sessions.add(webSocketSession2);
            }
            if(!sessions.isEmpty()) {
                UserMessageHandler.sendUserMessage(sessions,update);
            }
        sendUserMessage(update);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    public void sendUserMessage(UserMessagesDTO update) {
        messagingTemplate.convertAndSend("/topic/user/message/"+update.receiverId, update);
        messagingTemplate.convertAndSend("/topic/user/message/"+update.senderId, update);

    }
}
