package com.example.topgoback.Users.Service;

import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

    public void addOne(Passenger passenger)
    {
        passengerRepository.save(passenger);
    }

    public Passenger findById(Integer id)
    {

        return (passengerRepository.findById(id).get());
    }
}
