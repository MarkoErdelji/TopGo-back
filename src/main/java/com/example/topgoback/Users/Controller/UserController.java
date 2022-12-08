package com.example.topgoback.Users.Controller;

import com.example.topgoback.Enums.MessageType;
import com.example.topgoback.Messages.DTOS.SendMessageDTO;
import com.example.topgoback.Messages.DTOS.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Messages.Service.MessageService;
import com.example.topgoback.Notes.DTOS.NoteResponseDTO;
import com.example.topgoback.Notes.DTOS.UserNoteListDTO;
import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Service.NoteService;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RideService rideService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private NoteService noteService;

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<?> getUserRides(@PathVariable Integer id,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) String beginDateInterval,
                                                     @RequestParam(required = false) String endDateInterval)
    {
//        User user = userService.findOne(id);
//        if(user == null){
//            return new ResponseEntity<>("User does not exist!",HttpStatus.BAD_REQUEST);
//        }

        UserRidesListDTO rides = rideService.findRidesByUserId(id);


        if(rides == null){
            return new ResponseEntity<>("User has no rides!",HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(rides, HttpStatus.OK);
        }

    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size)
    {

        UserListDTO users = userService.findAll();

        if(users == null){
            return new ResponseEntity<>("No users in database",HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentialDTO loginCredentialDTO)
    {
        //Dodati logiku za kreiranje tokena za usera
        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

        jwtTokenDTO.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        jwtTokenDTO.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        return new ResponseEntity<>(jwtTokenDTO, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/message")
    public ResponseEntity<?> getUsersMessages(@PathVariable Integer id)
    {
//        User user = userService.findOne(id);
//
//        if(user == null){
//            return new ResponseEntity<>("No users in database",HttpStatus.NOT_FOUND);
//        }
        UserMessagesListDTO userMessages = messageService.findBySenderOrReceiver(id);

        if(userMessages == null){
            return new ResponseEntity<>("No messages found for user with id " + id,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userMessages,HttpStatus.OK);

    }


    @PostMapping(value = "{id}/message")
    public ResponseEntity<?> sendUsersMessage(@PathVariable Integer id, @RequestBody SendMessageDTO sendMessageDTO)
    {
//        User sender = userService.findOne(id);
//        User receiver = userService.findOne(sendMessageDTO.getReceiverId());
//        if(receiver == null){
//            return new ResponseEntity<>("User doesn't exist!",HttpStatus.NOT_FOUND);
//        }
//
//        if (sendMessageDTO.getType().compareTo(MessageType.RIDE) != 0){
//            sendMessageDTO.setRideId(null);
//        }

        UserMessagesDTO message = messageService.addOne(sendMessageDTO);
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    @PutMapping(value = "{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Integer id)
    {
        User user = userService.findOne(id);
        if(user == null){
            return new ResponseEntity<>("User doesn't exist!",HttpStatus.NOT_FOUND);
        }

        userService.blockUser(user);
        return new ResponseEntity<>("User is successfuly blocked",HttpStatus.OK);

    }

    @PutMapping(value = "{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Integer id)
    {
        User user = userService.findOne(id);
        if(user == null){
            return new ResponseEntity<>("User doesn't exist!",HttpStatus.NOT_FOUND);
        }

        userService.unblockUser(user);
        return new ResponseEntity<>("User is successfuly unblocked",HttpStatus.OK);

    }

    @PostMapping(value = "{id}/note")
    public ResponseEntity<?> addNote(@PathVariable Integer id,@RequestBody Note note)
    {
//        User user = userService.findOne(id);
//        if(user == null){
//            return new ResponseEntity<>("User doesn't exist!",HttpStatus.NOT_FOUND);
//        }

        NoteResponseDTO noteResponseDTO = noteService.addOne(note);

        return new ResponseEntity<>(noteResponseDTO,HttpStatus.OK);

    }


    @GetMapping(value = "{id}/note")
    public ResponseEntity<?> getUserNotes(@PathVariable Integer id,
                                        @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size)
    {

        UserNoteListDTO userNotes = noteService.findUsersNotes(id);

        if(userNotes == null){
            return new ResponseEntity<>("No notes in database",HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(userNotes, HttpStatus.OK);
        }
    }





}
