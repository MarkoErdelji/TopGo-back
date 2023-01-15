package com.example.topgoback.Users.Service;

import com.example.topgoback.AccountActivationToken.Service.AccountActivationTokenService;
import com.example.topgoback.Enums.UserType;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            passenger.setPassword(passengerDTO.getPassword());
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

    public Passenger update(CreatePassengerDTO createPassengerDTO, Integer id){
        Passenger passenger = findById(id);
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



}
