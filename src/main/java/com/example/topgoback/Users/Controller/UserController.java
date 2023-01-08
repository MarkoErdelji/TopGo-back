package com.example.topgoback.Users.Controller;

import com.example.topgoback.Enums.AllowedSortFields;
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
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.CredentialExpiredException;
import java.time.LocalDateTime;
import java.util.Arrays;

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
                                         @RequestParam(required = false,defaultValue = "0") Integer page,
                                         @RequestParam(required = false,defaultValue = "10") Integer size,
                                         @RequestParam(required = false,defaultValue = "id") String sort,
                                         @RequestParam(required = false,defaultValue = "0001-01-01T00:00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginDateInterval,
                                         @RequestParam(required = false,defaultValue = "9999-12-31T23:59:59.999999") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateInterval,
                                         Pageable pageable)
    {
        if(sort!=null){
            boolean isValidSortField = false;
            for (AllowedSortFields allowedField : AllowedSortFields.values()) {
                if (sort.equals(allowedField.getField())) {
                    isValidSortField = true;
                    break;
                }
            }
            if (!isValidSortField) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid sort field. Allowed fields: " + Arrays.toString(AllowedSortFields.values()));
            }
        }
        pageable = (Pageable) PageRequest.of(page, size, Sort.by(sort).ascending());

        UserRidesListDTO rides = rideService.findRidesByUserId(id,pageable,beginDateInterval,endDateInterval);


       return new ResponseEntity<>(rides,HttpStatus.OK);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getUsers(@RequestParam(required = false,defaultValue = "0") Integer page,
                                      @RequestParam(required = false,defaultValue = "10") Integer size,
                                      Pageable pageable)
    {
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
    public ResponseEntity<?> addNote(@PathVariable Integer id,@RequestBody CreateNoteDTO note)
    {
        NoteResponseDTO noteResponseDTO = noteService.addOne(id,note);

        return new ResponseEntity<>(noteResponseDTO,HttpStatus.OK);

    }


    @GetMapping(value = "{id}/note")
    public ResponseEntity<?> getUserNotes(@PathVariable Integer id,
                                          @RequestParam(required = false,defaultValue = "0") Integer page,
                                          @RequestParam(required = false,defaultValue = "10") Integer size,
                                          Pageable pageable)
    {

        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        UserNoteListDTO userNotes = noteService.findUsersNotes(id,pageable);
        return new ResponseEntity<>(userNotes, HttpStatus.OK);
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
