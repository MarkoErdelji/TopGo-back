package com.example.topgoback.Rides.Service;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;
    public void addOne(Ride ride) { rideRepository.save(ride);}

    public List<Ride> findRidesByUserId(int userId) {

       List<Ride> rides = rideRepository.findAll();

       List<Ride> userRides = new ArrayList<Ride>();
       for(Ride r : rides){
           if (r.getDriver().getId() == userId){
               userRides.add(r);
           }

        }
       if (userRides.isEmpty()){
           return null;
       }
       else {
           return userRides;
       }
        }
}
