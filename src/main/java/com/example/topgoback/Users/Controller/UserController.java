package com.example.topgoback.Users.Controller;

import com.example.topgoback.Email.Model.Email;
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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.CredentialExpiredException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping(value = "api/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
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

    UserController(){}

    @GetMapping(value = "{id}/ride")
    public ResponseEntity<?> getUserRides(@Valid @PathVariable Integer id,
                                          @Valid @RequestParam(required = false,defaultValue = "0") Integer page,
                                          @Valid @RequestParam(required = false,defaultValue = "10") Integer size,
                                          @Valid @RequestParam(required = false,defaultValue = "id") String sort,
                                          @Valid @RequestParam(required = false,defaultValue = "0001-01-01T00:00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginDateInterval,
                                          @Valid @RequestParam(required = false,defaultValue = "9999-12-31T23:59:59.999999") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateInterval,
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
    public ResponseEntity<?> getUsers(@Valid @RequestParam(required = false,defaultValue = "0") Integer page,
                                      @Valid @RequestParam(required = false,defaultValue = "10") Integer size,
                                      Pageable pageable)
    {
        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        UserListDTO users = userService.findAll(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid  @RequestBody LoginCredentialDTO loginCredentialDTO)
    {
        JWTTokenDTO jwtTokenDTO = userService.login(loginCredentialDTO);

        return new ResponseEntity<>(jwtTokenDTO,HttpStatus.OK);

    }


    @PutMapping(value = "{id}/changePassword",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeUserPassword(@Valid  @PathVariable Integer id,@Valid @RequestBody ChangePasswordDTO changePasswordDTO)
    {
        userService.changeUserPassword(id, changePasswordDTO);

        ResponseEntity re = new ResponseEntity<>("Password successfully changed!",HttpStatus.NO_CONTENT);
        System.out.println(re);

        return new ResponseEntity<>("Password successfully changed!",HttpStatus.NO_CONTENT);
    }



    @GetMapping(value = "{id}/message")
    public ResponseEntity<?> getUsersMessages(@Valid  @PathVariable Integer id,
                                              @Valid  @RequestParam(required = false,defaultValue = "0") Integer page,
                                              @Valid  @RequestParam(required = false,defaultValue =  "0") Integer size,
                                              Pageable pageable)
    {

        if (size == 0 || size == Pageable.unpaged().getPageSize()) {
            pageable = PageRequest.of(pageable.getPageNumber(), Integer.MAX_VALUE, pageable.getSort());
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }

        UserMessagesListDTO userMessages = messageService.findBySenderOrReceiver(id,pageable);
        return new ResponseEntity<>(userMessages,HttpStatus.OK);

    }


    @PostMapping(value = "{id}/message")
    public ResponseEntity<?> sendUsersMessage(@Valid @PathVariable Integer id, @Valid @RequestBody SendMessageDTO sendMessageDTO)
    {
        UserMessagesDTO message = messageService.addOne(id,sendMessageDTO);
        return new ResponseEntity<>(message,HttpStatus.OK);

    }

    @PutMapping(value = "{id}/block")
    public ResponseEntity<?> blockUser(@Valid @PathVariable Integer id)
    {
        userService.blockUser(id);
        return new ResponseEntity<>("User is successfuly blocked",HttpStatus.NO_CONTENT);

    }

    @PutMapping(value = "{id}/unblock")
    public ResponseEntity<?> unblockUser(@Valid @PathVariable Integer id)
    {
        userService.unblockUser(id);
        return new ResponseEntity<>("User is successfuly unblocked",HttpStatus.NO_CONTENT);

    }

    @PostMapping(value = "{id}/note")
    public ResponseEntity<?> addNote(@Valid @PathVariable Integer id,@Valid @RequestBody CreateNoteDTO note)
    {
        NoteResponseDTO noteResponseDTO = noteService.addOne(id,note);

        return new ResponseEntity<>(noteResponseDTO,HttpStatus.OK);

    }


    @GetMapping(value = "{id}/note")
    public ResponseEntity<?> getUserNotes(@Valid @PathVariable Integer id,
                                          @Valid @RequestParam(required = false,defaultValue = "0") Integer page,
                                          @Valid @RequestParam(required = false,defaultValue = "10") Integer size,
                                          Pageable pageable)
    {

        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        UserNoteListDTO userNotes = noteService.findUsersNotes(id,pageable);
        return new ResponseEntity<>(userNotes, HttpStatus.OK);
    }


    @GetMapping(value="{id}/resetPassword")
    public ResponseEntity<?> sendEmail(@Valid @PathVariable Integer id) throws MessagingException, IOException {
        userService.sendEmail(id);
        return new ResponseEntity<>("Email with reset code has been sent!",HttpStatus.NO_CONTENT);
    }

    @PutMapping(value="{id}/resetPassword")
    public ResponseEntity<?> resetUserPassword(@Valid @PathVariable Integer id,@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetUserPassword(id,resetPasswordDTO);
        return new ResponseEntity<>("Password successfully changed!",HttpStatus.NO_CONTENT);
    }



    @GetMapping(value = "/{email}")
    public ResponseEntity<?> getUserRefByEmail(@Valid @PathVariable String email)
    {
        try{
            UserRef user = userService.loadUserReferenceByUsername(email);

            return ResponseEntity.ok(user);
        } catch(UsernameNotFoundException unfe){
            return new ResponseEntity<>("Email not found",HttpStatus.NOT_FOUND);

        }

    }


}
