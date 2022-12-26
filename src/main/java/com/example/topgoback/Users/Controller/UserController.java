package com.example.topgoback.Users.Controller;

import com.example.topgoback.Email.Model.Email;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Messages.DTO.SendMessageDTO;
import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Service.MessageService;
import com.example.topgoback.Notes.DTO.NoteResponseDTO;
import com.example.topgoback.Notes.DTO.UserNoteListDTO;
import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Service.NoteService;
import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import com.example.topgoback.PasswordResetTokens.Service.PasswordResetTokenService;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UserService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;

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
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    UserController(JavaMailSender mailSender){this.mailSender = mailSender;}

    @PreAuthorize(value="hasAuthority('ROLE_USER')")
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

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginCredentialDTO loginCredentialDTO)
    {
        try {
            final User userDetails = userService
                    .login(loginCredentialDTO);
            final String token = jwtTokenUtil.generateToken(userDetails);

            JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

            jwtTokenDTO.setAccessToken(token);
            jwtTokenDTO.setRefreshToken(token);

            return ResponseEntity.ok(jwtTokenDTO);
        }
        catch (Exception e){
            return new ResponseEntity<>("Wrong username or password!",HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping(value = "{id}/resetPassword",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@PathVariable Integer id,@RequestBody LoginCredentialDTO loginCredentialDTO)
    {
        final User userDetails = userService
                .loadUserByUsername(loginCredentialDTO.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

        jwtTokenDTO.setAccessToken(token);
        jwtTokenDTO.setRefreshToken(token);

        return ResponseEntity.ok(jwtTokenDTO);

    }


//    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> emailUserPasswordReset(@RequestBody Email email)
//    {
//        MimeMessage message = mailSender.createMimeMessage();
//        try{
//            userService.loadUserByUsername(email.getTo());
//        }
//        catch (UsernameNotFoundException e){
//            return new ResponseEntity<>("No such user",HttpStatus.NOT_FOUND);
//        }
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setTo(email.getTo());
//        helper.setSubject(email.getSubject());
//        helper.setText(email.getMessage(),true);
//        mailSender.send(message);
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//
//    }


//    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> resetUserPassword(@RequestBody LoginCredentialDTO loginCredentialDTO)
//    {
//        final User userDetails = userService
//                .loadUserByUsername(loginCredentialDTO.getEmail());
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();
//
//        jwtTokenDTO.setAccessToken(token);
//        jwtTokenDTO.setRefreshToken(token);
//
//        return ResponseEntity.ok(jwtTokenDTO);
//
//    }



    @GetMapping(value = "{id}/message")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
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
//        User user = userService.findOne(id);
//        if(user == null){
//            return new ResponseEntity<>("User doesn't exist!",HttpStatus.NOT_FOUND);
//        }
//
//        userService.blockUser(user);
        return new ResponseEntity<>("User is successfuly blocked",HttpStatus.NO_CONTENT);

    }

    @PutMapping(value = "{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Integer id)
    {
//        User user = userService.findOne(id);
//        if(user == null){
//            return new ResponseEntity<>("User doesn't exist!",HttpStatus.NOT_FOUND);
//        }
//
//        userService.unblockUser(user);
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
    @PreAuthorize(value = "hasRole('USER')")
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
        PasswordResetToken passwordResetToken = passwordResetTokenService.findOne(token);
        User user = userService.loadUserByUsername(email);

        user.setPassword(newPassword);
        userService.updateOne(user);
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



    @PostAuthorize("hasAuthority('ROLE_DRIVER')")
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
