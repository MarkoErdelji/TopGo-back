package com.example.topgoback.Users.Service;

import com.example.topgoback.Users.DTO.CreatePassengerDTO;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

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

    public List<Passenger> findAll(){
        return passengerRepository.findAll();
    }

    public List<Passenger> getPaginated(Integer page, Integer size){
        List<Passenger> allPassengers = findAll();
        List<Passenger> filteredPassengers = new ArrayList<Passenger>();
        for(Integer i = page; i <=size; i ++){
            filteredPassengers.add(allPassengers.get(i));
        }
        return filteredPassengers;
    }
}
