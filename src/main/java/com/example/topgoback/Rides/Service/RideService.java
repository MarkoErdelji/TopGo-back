package com.example.topgoback.Rides.Service;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;
    public void addOne(Ride ride) { rideRepository.save(ride);}
}
