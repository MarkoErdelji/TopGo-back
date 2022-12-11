package com.example.topgoback.Rides.Controller;

import com.example.topgoback.PanicReport.DTO.PanicReportDTO;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import org.hibernate.mapping.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<RideDTO> save(@RequestBody CreateRideDTO createRideDTO){

        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }
    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideDTO> getActiveRideForDriver(@PathVariable Integer driverId){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideDTO> getActiveRideForPassenger(@PathVariable Integer passengerId){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideDTO> withdrawRoute(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic", consumes = "application/json")
    public ResponseEntity<PanicReportDTO> panic(@PathVariable Integer id, @RequestBody String reason){
        return new ResponseEntity<>(PanicReportDTO.getMokap(), HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<RideDTO> acceptRoute(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getAcceptedMockupData(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    public ResponseEntity<RideDTO> finishRoute(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getFinishMockupData(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel", consumes = "application/json")
    public ResponseEntity<RideDTO> cancelRoute(@PathVariable Integer id, @RequestBody String reason){
        return new ResponseEntity<>(RideDTO.getCanceledMockupData(), HttpStatus.OK);
    }

}
