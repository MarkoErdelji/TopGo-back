package com.example.topgoback.Users.Service;

import com.example.topgoback.AccountActivationToken.Service.AccountActivationTokenService;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private AccountActivationTokenService activationTokenService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public PassengerService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public Passenger addOne(CreatePassengerDTO passengerDTO) throws MessagingException, IOException {
        Optional<Passenger> user = Optional.ofNullable(passengerRepository.findByEmail(passengerDTO.getEmail()));
        if(user.isEmpty()){
            Passenger passenger = new Passenger();
            passenger.setFirstName(passengerDTO.getName());
            passenger.setLastName(passengerDTO.getSurname());
            passenger.setProfilePicture(passengerDTO.getProfilePicture());
            passenger.setPhoneNumber(passengerDTO.getTelephoneNumber());
            passenger.setEmail(passengerDTO.getEmail());
            passenger.setAddress(passengerDTO.getAddress());
            passenger.setPassword(passwordEncoder.encode(passengerDTO.getPassword()));
            passenger.setUserType(UserType.USER);
            passengerRepository.save(passenger);
            activationTokenService.addOne(passenger.getId());
            sendEmail(passenger.getId());
            return passenger;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with that email already exists!");

    }
    public void sendEmail(int passengerId) throws IOException, MessagingException {
        Passenger passenger = findById(passengerId);
        Path filePath = Paths.get("src/main/resources/ActivateAccount.html");
        String emailTemplate = Files.readString(filePath, StandardCharsets.UTF_8);
        emailTemplate = emailTemplate.replace("{{action_url}}","http://localhost:8000/api/passenger/activate/"+passengerId);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(passenger.getEmail());
        helper.setSubject("topGoAppRS@gmail.com");
        helper.setText(emailTemplate,true);
        mailSender.send(message);
    }
    public Passenger findById(Integer id)
    {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isPresent()){
            return passenger.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger does not exist!");
    }

    public Passenger update(UpdatePassengerDTO createPassengerDTO, Integer id){
        Passenger passenger = findById(id);
        passenger.setFirstName(createPassengerDTO.getName());
        passenger.setLastName(createPassengerDTO.getSurname());
        passenger.setProfilePicture(createPassengerDTO.getProfilePicture());
        passenger.setPhoneNumber(createPassengerDTO.getTelephoneNumber());
        passenger.setAddress(createPassengerDTO.getAddress());
        passenger.setEmail(createPassengerDTO.getEmail());
        passengerRepository.save(passenger);
        return passenger;
    }

    public void delete(int id){
        passengerRepository.deleteById(id);
    }

    public void activate(int id){
        Passenger passenger = findById(id);
        passenger.setActive(true);
        passengerRepository.save(passenger);

    }

    public PassengerListDTO findAll(Pageable pageable){
        Page<Passenger> page = passengerRepository.findAll( pageable);
        List<PassengerListResponseDTO> passengerListResponseDTOS = PassengerListResponseDTO.convertToUserListResponseDTO(page.getContent());

        PassengerListDTO passengers = new PassengerListDTO(new PageImpl<>(passengerListResponseDTOS,pageable, page.getTotalElements()));
        return passengers;
    }


    public List<RideDTO> getPassengerFinishedRides(String authorization) {
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int id = jwtTokenUtil.getUserIdFromToken(jwtToken);
        List<Ride> rides = passengerRepository.findRidesByPassengerIdAndIsFinished(id);
        List<RideDTO> ridesDTO = new ArrayList<>();
        for (Ride ride:rides)
        {
            ridesDTO.add(new RideDTO(ride));
        }
        return ridesDTO;

    }

    public InviteFriendDTO inviteFriend(String authorization, int id) {
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        InviteFriendDTO response = new InviteFriendDTO();
        response.setInvitedId(id);
        response.setUserId(userId);
        response.setStatus("PENDING");
        sendFriendInvite(response);
        return response;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    public void sendFriendInvite(InviteFriendDTO update) {
        messagingTemplate.convertAndSend("/topic/passenger/invites/"+update.getInvitedId(), update);

    }
    @CrossOrigin(origins = "http://localhost:4200")
    public void sendFriendInviteResponse(InviteFriendDTO update) {
        messagingTemplate.convertAndSend("/topic/passenger/response/"+update.getUserId(), update);

    }

    public InviteFriendDTO inviteResponse(InviteFriendDTO response) {
        sendFriendInviteResponse(response);
        return response;
    }
}
