package com.example.topgoback.Users.Controller;

import com.example.topgoback.AccountActivationToken.Service.AccountActivationTokenService;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/passenger")
@CrossOrigin(origins = "http://localhost:4200")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    @Autowired
    private AccountActivationTokenService activationTokenService;

    @Autowired
    private RideService rideService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody CreatePassengerDTO createPassengerDTO) {

    Passenger passenger = passengerService.addOne(createPassengerDTO);
    return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);


    }

    @GetMapping(value = "/{id}")
    public  ResponseEntity<CreatePassengerResponseDTO> getOne(@PathVariable Integer id){
        Passenger passenger = passengerService.findById(id);

        return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);

    }
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<CreatePassengerResponseDTO> updateOne(@PathVariable Integer id, @RequestBody CreatePassengerDTO createPassengerDTO){
        Passenger passenger = new Passenger();
        passenger = passengerService.update(createPassengerDTO, id);

        return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);
    }
    @GetMapping
    public  ResponseEntity<?> getPaginated(@RequestParam(required = false) Integer page,
                                           @RequestParam(required = false) Integer size,
                                           Pageable pageable){
        if(page == null){
            page = 0;
        }
        if(size == null){
            size = 10;
        }
        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        PassengerListDTO passengers = passengerService.findAll(pageable);
        return new ResponseEntity<>(passengers, HttpStatus.OK);

    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<?> getActivation(@PathVariable int activationId){
        activationTokenService.findAndActivate(activationId);
        return new ResponseEntity<>("Succesfull account activation", HttpStatus.OK);
    }

    @GetMapping(value = "{id}/ride")
    public ResponseEntity<?> getRides(@PathVariable Integer id,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer size,
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(required = false) String beginDateInterval,
                                          @RequestParam(required = false) String endDateInterval) {

        UserRidesListDTO rides = rideService.findRidesByPassengerId(id);
        if(rides == null){
            return new ResponseEntity<>("Passenger has no rides!",HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(rides, HttpStatus.OK);
        }
    }
}
