package com.example.topgoback.Users.Controller;

import com.example.topgoback.Users.DTO.CreatePassengerDTO;
import com.example.topgoback.Users.DTO.CreatePassengerResponseDTO;
import com.example.topgoback.Users.DTO.PassengerListResponseDTO;
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
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<CreatePassengerResponseDTO> create(@RequestBody CreatePassengerDTO createPassengerDTO){

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
        Passenger passenger = passengerService.findById(id);
        passenger = passengerService.update(createPassengerDTO, passenger);

        return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);
    }
    @GetMapping(consumes = "application/json")
    public  ResponseEntity<PassengerListResponseDTO> getPaginated(@RequestParam Integer page,
                                                                  @RequestParam Integer size){
        List<Passenger> passengers = passengerService.findAll();
        List<CreatePassengerResponseDTO> passengerListResponseDTOS = passengerService.convertToDTOList(passengers);

        return new ResponseEntity<>(new PassengerListResponseDTO(passengerListResponseDTOS), HttpStatus.OK);

    }
}
