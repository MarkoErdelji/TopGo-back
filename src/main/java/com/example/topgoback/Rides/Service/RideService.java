package com.example.topgoback.Rides.Service;

import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.PassengerService;
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

    @Autowired
    private PassengerService passengerService;

    public void addOne(Ride ride) { rideRepository.save(ride);}

    public UserRidesListDTO findRidesByUserId(int userId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        User user = userService.findOne(userId);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndBeginBetween(user.getId(),beginDateTimeInterval,endDateTimeInterval,pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;

        }

    public UserRidesListDTO findRidesByPassengerId(int passengerId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        Passenger passenger = passengerService.findById(passengerId);
        Page<Ride> rides = rideRepository.findByPassengerAndBeginBetween(passenger.getId(),beginDateTimeInterval,endDateTimeInterval,pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;
    }
}
