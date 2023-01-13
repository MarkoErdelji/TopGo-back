package com.example.topgoback.Rides.Controller;

import com.example.topgoback.FavouriteRides.DTO.FavouriteRideDTO;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.UserRef;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping(value = "api/ride")
public class RideController {

    @Autowired
    private RideService rideService;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PostMapping(consumes = "application/json")
    public ResponseEntity<RideDTO> createRide(@Valid @RequestBody CreateRideDTO createRideDTO){
        RideDTO response = rideService.createRide(createRideDTO);
        sendDriverRideUpdate(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/driver/{driverId}/active")
    public ResponseEntity<RideDTO> getActiveRideForDriver(@PathVariable Integer driverId){
        RideDTO response = rideService.getDriverActiveRide(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/driver/{driverId}/accepted")
    public ResponseEntity<RideDTO> getAcceptedRideForDriver(@PathVariable Integer driverId){
        RideDTO response = rideService.getDriverAcceptedRide(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    public ResponseEntity<RideDTO> getActiveRideForPassenger(@PathVariable Integer passengerId){
        RideDTO response = rideService.getPassengerActiveRide(passengerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Integer id){
        RideDTO response = rideService.getRideById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    public ResponseEntity<RideDTO> withdrawRoute(@PathVariable Integer id){
        RideDTO response = rideService.withdrawRide(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic", consumes = "application/json")
    public ResponseEntity<PanicDTO> panic(@RequestHeader("Authorization") String authorization,
                                          @PathVariable Integer id,
                                          @RequestBody RejectionTextDTO reason)
    {

        PanicDTO response = rideService.panic(id,reason,authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/accept")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Integer id){
        RideDTO ride = rideService.acceptRide(id);
        sendPassengerRideUpdate(ride);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Integer id, @RequestBody RejectionTextDTO reason){
        RideDTO ride = rideService.cancelRide(id,reason);
        sendPassengerRideUpdate(ride);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    public ResponseEntity<RideDTO> finishRoute(@PathVariable Integer id){
        RideDTO ride = rideService.endRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/start")
    public ResponseEntity<RideDTO> startRoute(@PathVariable Integer id){
        RideDTO ride = rideService.startRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }
    @PostMapping(value = "/favourites", consumes = "application/json")
    public ResponseEntity<FavouriteRideInfoDTO> addFavouriteRide(@RequestBody FavouriteRideDTO favouriteRide){
        FavouriteRideInfoDTO response = rideService.addFavouriteRide(favouriteRide);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/favourites")
    public ResponseEntity<List<FavouriteRideInfoDTO>> getFavouriteRides(@RequestHeader("Authorization") String authorization)
    {
        List<FavouriteRideInfoDTO> response = rideService.getFavouriteRides(authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);


    }
    @DeleteMapping(value = "/favourites/{id}")
    public ResponseEntity<String> deleteFavouriteRides(@PathVariable Integer id)
    {
        rideService.deleteFavouriteRides(id);
        return new ResponseEntity<>("Successful deletion of favorite location!",HttpStatus.NO_CONTENT);


    }





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
