package com.example.topgoback.Users.Controller;

import com.example.topgoback.Messages.DTO.SendMessageDTO;
import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Service.MessageService;
import com.example.topgoback.Notes.DTO.NoteResponseDTO;
import com.example.topgoback.Notes.DTO.UserNoteListDTO;
import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Service.NoteService;
import com.example.topgoback.PasswordResetTokens.Service.PasswordResetTokenService;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialExpiredException;

@RestController
@RequestMapping(value = "api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private UserService userService;

    @Autowired
    private RideService rideService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    UserController(JavaMailSender mailSender){this.mailSender = mailSender;}

    @GetMapping(value = "{id}/ride")
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
                                      @RequestParam(required = false) Integer size,
                                      Pageable pageable)
    {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 3;
        }
        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        UserListDTO users = userService.findAll(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginCredentialDTO loginCredentialDTO)
    {
        JWTTokenDTO jwtTokenDTO = userService.login(loginCredentialDTO);

        return new ResponseEntity<>(jwtTokenDTO,HttpStatus.OK);

    }


    @PutMapping(value = "{id}/changePassword",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeUserPassword(@PathVariable Integer id,@RequestBody ChangePasswordDTO changePasswordDTO)
    {
        userService.changeUserPassword(id, changePasswordDTO);

        return new ResponseEntity<>("Password successfully changed!",HttpStatus.NO_CONTENT);
    }



    @GetMapping(value = "{id}/message")
    public ResponseEntity<?> getUsersMessages(@PathVariable Integer id)
    {

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
        userService.blockUser(id);
        return new ResponseEntity<>("User is successfuly blocked",HttpStatus.NO_CONTENT);

    }

    @PutMapping(value = "{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Integer id)
    {
        userService.unblockUser(id);
        return new ResponseEntity<>("User is successfuly unblocked",HttpStatus.NO_CONTENT);

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


    @PutMapping(value = "reset/{token}/{email}/{newPassword}")
    public ResponseEntity<?> updateUserPasswordWithToken(@PathVariable String token, @PathVariable String email,@PathVariable String newPassword)
    {
        try{
        passwordResetTokenService.findOne(token);
        User user = userService.loadUserByUsername(email);

        userService.updateUserPassword(user,newPassword);
        } catch( CredentialExpiredException ce){
            return new ResponseEntity<>("Token expired", HttpStatus.EXPECTATION_FAILED);
        }
        catch(UsernameNotFoundException unfe){
            return new ResponseEntity<>("Email not found",HttpStatus.NOT_FOUND);

        }
        catch (Exception e){
            return new ResponseEntity<>("Token not found", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Password successfuly updated",HttpStatus.OK);


    }



    @GetMapping(value = "/{email}")
    public ResponseEntity<?> getUserRefByEmail(@PathVariable String email)
    {
        try{
            UserRef user = userService.loadUserReferenceByUsername(email);

            return ResponseEntity.ok(user);
        } catch(UsernameNotFoundException unfe){
            return new ResponseEntity<>("Email not found",HttpStatus.NOT_FOUND);

        }

    }


}
