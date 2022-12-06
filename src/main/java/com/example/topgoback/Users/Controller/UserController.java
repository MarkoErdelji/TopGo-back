package com.example.topgoback.Users.Controller;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/user/")
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
        UserRidesListDTO userRidesDTO = new UserRidesListDTO();
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

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size)
    {
        UserListDTO userListDTO = new UserListDTO();
        List<User> users = userService.findAll();

        if(users == null){
            return new ResponseEntity<>("No users in database",HttpStatus.NOT_FOUND);
        }
        else {
            userListDTO.setTotalCount(users.size());
            userListDTO.setResults((ArrayList<UserListResponseDTO>) UserListResponseDTO.convertToUserListResponseDTO(users));
            return new ResponseEntity<>(userListDTO, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentialDTO loginCredentialDTO)
    {
        //Dodati logiku za kreiranje tokena za usera
        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO();

        jwtTokenDTO.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        jwtTokenDTO.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        return new ResponseEntity<>(jwtTokenDTO, HttpStatus.OK);
    }






}
