package com.example.topgoback.Rides.Service;

import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UnregisteredUserService;
import com.example.topgoback.Users.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserService userService;

    public void addOne(Ride ride) { rideRepository.save(ride);}

    public UserRidesListDTO findRidesByUserId(int userId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        User user = userService.findOne(userId);
        System.out.println("SELECT r FROM Ride r JOIN r.passenger p JOIN r.driver d WHERE p.id = " + userId + " OR d.id = " + userId + " AND r.start BETWEEN " + beginDateTimeInterval + " AND " + endDateTimeInterval);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndBeginBetween(user.getId(),beginDateTimeInterval,endDateTimeInterval,pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;

        }
    public UserRidesListDTO findRidesByPassengerId(int passengerId) {

        UserRidesListDTO userRidesListDTO = new UserRidesListDTO();
        userRidesListDTO.setTotalCount(243);
        ArrayList<UserRideDTO> userRides = new ArrayList<>();
        userRides.add(UserRideDTO.getMockupData());
        userRidesListDTO.setResults(userRides);

        return userRidesListDTO;
    }
}
