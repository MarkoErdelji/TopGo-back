package com.example.topgoback.Rides.Service;

import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;
    public void addOne(Ride ride) { rideRepository.save(ride);}

    public UserRidesListDTO findRidesByUserId(int userId) {

        UserRidesListDTO userRidesListDTO = new UserRidesListDTO();
        userRidesListDTO.setTotalCount(243);
        ArrayList<UserRideDTO> userRides = new ArrayList<>();
        userRides.add(UserRideDTO.getMockupData());
        userRidesListDTO.setResults(userRides);

        return userRidesListDTO;

//       List<Ride> rides = rideRepository.findAll();
//
//       List<Ride> userRides = new ArrayList<Ride>();
//       for(Ride r : rides){
//           if (r.getDriver().getId() == userId){
//               userRides.add(r);
//           }
//
//        }
//       if (userRides.isEmpty()){
//           return null;
//       }
//       else {
//           return userRides;
//       }
        }
}
