package com.example.topgoback.Rides.Controller;

import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/rides")
public class RideController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RideResponseDTO> save(@RequestBody CreateRideDTO createRideDTO){

        return new ResponseEntity<>(new RideResponseDTO(createRideDTO), HttpStatus.OK);
    }
}
