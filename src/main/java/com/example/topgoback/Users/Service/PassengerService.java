package com.example.topgoback.Users.Service;

import com.example.topgoback.AccountActivationToken.Service.AccountActivationTokenService;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

    public Passenger addOne(CreatePassengerDTO passengerDTO) {
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
            return passenger;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with that email already exists!");

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
