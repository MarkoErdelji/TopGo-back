package com.example.topgoback.Users.Controller;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.CreatePassengerDTO;
import com.example.topgoback.Users.DTO.CreateUserDTO;
import com.example.topgoback.Users.DTO.UserRidesDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RideService rideService;

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<?> getUserRides(@PathVariable Integer id,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) String beginDateInterval,
                                                     @RequestParam(required = false) String endDateInterval)
    {
        UserRidesDTO userRidesDTO = new UserRidesDTO();
        List<Ride> rides = rideService.findRidesByUserId(id);

        if(rides == null){
            return new ResponseEntity<>("User does not exist",HttpStatus.NOT_FOUND);
        }
        else {
            userRidesDTO.setTotalCount(rides.size());
            userRidesDTO.setResults((ArrayList<Ride>) rides);
            return new ResponseEntity<>(userRidesDTO, HttpStatus.OK);
        }
    }






}
