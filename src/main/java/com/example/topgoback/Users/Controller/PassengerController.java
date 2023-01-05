package com.example.topgoback.Users.Controller;

import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/passenger")
@CrossOrigin(origins = "http://localhost:4200")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

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
    public  ResponseEntity<?> getPaginated(@RequestParam Integer page,
                                                                  @RequestParam Integer size){
        PassengerListDTO passengers = passengerService.findAll();


        return new ResponseEntity<>(passengers, HttpStatus.OK);

    }

    @GetMapping(value = "/activate/{activationId}")
    public ResponseEntity<?> getActivation(){
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
