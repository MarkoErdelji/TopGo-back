package com.example.topgoback.Users.Service;

import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private UserRepository userRepository;

    public Passenger addOne(CreatePassengerDTO passengerDTO)
    {
        Passenger passenger = new Passenger();
        passenger.setFirstName(passengerDTO.getName());
        passenger.setLastName(passengerDTO.getSurname());
        passenger.setProfilePicture(passengerDTO.getProfilePicture());
        passenger.setPhoneNumber(passengerDTO.getTelephoneNumber());
        passenger.setEmail(passengerDTO.getEmail());
        passenger.setAddress(passengerDTO.getAddress());
        passenger.setPassword(passengerDTO.getPassword());

        userRepository.save((User) passenger);
        passengerRepository.save(passenger);
        return passenger;
    }

    public Passenger findById(Integer id)
    {

        return (passengerRepository.findById(id).orElseGet(null));
    }

    public Passenger update(CreatePassengerDTO createPassengerDTO, Passenger passenger){
        passenger.setFirstName(createPassengerDTO.getName());
        passenger.setLastName(createPassengerDTO.getSurname());
        passenger.setProfilePicture(createPassengerDTO.getProfilePicture());
        passenger.setPhoneNumber(createPassengerDTO.getTelephoneNumber());
        passenger.setEmail(createPassengerDTO.getEmail());
        passenger.setAddress(createPassengerDTO.getAddress());
        passenger.setPassword(createPassengerDTO.getPassword());

        passengerRepository.save(passenger);
        return passenger;
    }

    public PassengerListDTO findAll(){
        PassengerListDTO passengerListDTo = new PassengerListDTO();
        passengerListDTo.setTotalCount(243);
        ArrayList<PassengerListResponseDTO> passengerListResponseDTOS = new ArrayList<>();
        passengerListResponseDTOS.add(PassengerListResponseDTO.getMockupData());
        passengerListDTo.setResults(passengerListResponseDTOS);
        if(passengerListDTo.getResults().isEmpty()){
            return null;}
        else{
            return passengerListDTo;
        }
    }

}
