package com.example.topgoback.Rides.Controller;

import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Vehicles.Model.VehicleType;
import com.example.topgoback.Vehicles.Service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    @Autowired
    private RideService rideService;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping(consumes = "application/json")
    public ResponseEntity<RideDTO> createRide(@RequestBody CreateRideDTO createRideDTO){
        RideDTO response = rideService.createRide(createRideDTO);
        sendRideUpdate(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /*@GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideDTO> getActiveRideForDriver(@PathVariable Integer driverId){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }*/

   /* @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideDTO> getActiveRideForPassenger(@PathVariable Integer passengerId){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }*/

   /* @GetMapping(value = "/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }*/

   /* @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideDTO> withdrawRoute(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getMockupData(), HttpStatus.OK);
    }*/

    @PutMapping(value = "/{id}/panic", consumes = "application/json")
    public ResponseEntity<PanicDTO> panic(@PathVariable Integer id, @RequestBody String reason){
        return new ResponseEntity<>(PanicDTO.getMokap(), HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Integer id){
        RideDTO ride = rideService.acceptRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

   /* @PutMapping(value = "/{id}/end")
    public ResponseEntity<RideDTO> finishRoute(@PathVariable Integer id){
        return new ResponseEntity<>(RideDTO.getFinishMockupData(), HttpStatus.OK);
    }*/

//    @PutMapping(value = "/{id}/cancel", consumes = "application/json")
//    public ResponseEntity<RideDTO> cancelRoute(@PathVariable Integer id, @RequestBody String reason){
//        //return new ResponseEntity<>(RideDTO.getCanceledMockupData(), HttpStatus.OK);
//    }


    @CrossOrigin(origins = "http://localhost:4200")

    public void sendDriverRideUpdate(RideDTO update) {
        messagingTemplate.convertAndSend("/topic/driver/ride/"+update.driver.getId(), update);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    public void sendPassengerRideUpdate(RideDTO update) {
        for(UserRef p: update.getPassengers()){
            messagingTemplate.convertAndSend("/topic/passenger/ride/"+p.getId(), update);
        }
    }
}
