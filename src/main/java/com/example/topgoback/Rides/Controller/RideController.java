package com.example.topgoback.Rides.Controller;

import com.example.topgoback.FavouriteRides.DTO.FavouriteRideDTO;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
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
    @PreAuthorize("hasAnyRole('USER')")
    @Valid
    public ResponseEntity<RideDTO> createRide(@Valid  @RequestBody CreateRideDTO createRideDTO){
        RideDTO response = rideService.createRide(createRideDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/driver/{driverId}/active")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> getActiveRideForDriver(@PathVariable Integer driverId){
        RideDTO response = rideService.getDriverActiveRide(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/driver/{driverId}/accepted")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> getAcceptedRideForDriver(@PathVariable Integer driverId){
        RideDTO response = rideService.getDriverAcceptedRide(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/driver/{driverId}/finished")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('DRIVER')")
    public ResponseEntity<?> getFinishedRidesForDriver(@PathVariable Integer driverId){
        List<RideDTO> response = rideService.getDriverFinishedRides(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/passenger/{passengerId}/active")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('USER')")
    public ResponseEntity<RideDTO> getActiveRideForPassenger(@PathVariable Integer passengerId){
        RideDTO response = rideService.getPassengerActiveRide(passengerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('DRIVER') || hasAnyRole('USER')")
    public ResponseEntity<RideDTO> getRide(@PathVariable Integer id){
        RideDTO response = rideService.getRideById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/withdraw")
    @Valid
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<RideDTO> withdrawRoute(@PathVariable Integer id){
        RideDTO response = rideService.withdrawRide(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/panic", consumes = "application/json")
    @Valid
    @PreAuthorize("hasAnyRole('USER') || hasAnyRole('DRIVER')")
    public ResponseEntity<PanicDTO> panic(@RequestHeader("Authorization") String authorization,
                                          @PathVariable Integer id,
                                          @Valid @Nullable @RequestBody RejectionTextDTO reason)
    {

        PanicDTO response = rideService.panic(id,reason,authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/accept")
    @Valid
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Integer id){
        RideDTO ride = rideService.acceptRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }


    @GetMapping(value = "passenger/{id}/pending")
    public ResponseEntity<?> getPassengerPendingRides(@PathVariable Integer id){
        RideDTO rideDTO = rideService.findRideByPassengerAndIsPending(id);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @GetMapping(value = "passenger/{id}/accepted")
    public ResponseEntity<?> getPassengerAcceptedRides(@PathVariable Integer id){
        RideDTO rideDTO = rideService.getPassengersAcceptedRide(id);
        return new ResponseEntity<>(rideDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/cancel")
    @Valid
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Integer id, @Nullable @RequestBody RejectionTextDTO reason){
        RideDTO ride = rideService.cancelRide(id,reason);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }


    @PutMapping(value = "/{id}/decline")
    @Valid
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> declineRide(@PathVariable Integer id, @Nullable @RequestBody RejectionTextDTO rejectionTextDTO){
        RideDTO ride = rideService.declineRide(id, rejectionTextDTO);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/end")
    @Valid
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> finishRoute(@PathVariable Integer id){
        RideDTO ride = rideService.endRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/start")
    @Valid
    @PreAuthorize("hasAnyRole('DRIVER')")
    public ResponseEntity<RideDTO> startRoute(@PathVariable Integer id){
        RideDTO ride = rideService.startRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }
    @PostMapping(value = "/favorites", consumes = "application/json")
    @Valid
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<FavouriteRideInfoDTO> addFavouriteRide(@Valid @RequestBody @Nullable FavouriteRideDTO favouriteRide){
        FavouriteRideInfoDTO response = rideService.addFavouriteRide(favouriteRide);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/favorites")
    @Valid
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<FavouriteRideInfoDTO>> getFavouriteRides(@RequestHeader("Authorization") String authorization)
    {
        List<FavouriteRideInfoDTO> response = rideService.getFavouriteRides(authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);


    }
    @DeleteMapping(value = "/favorites/{id}")
    @Valid
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> deleteFavouriteRides(@PathVariable Integer id)
    {
        rideService.deleteFavouriteRides(id);
        return new ResponseEntity<>("Successful deletion of favorite location!",HttpStatus.NO_CONTENT);


    }

    @PutMapping(value="/simulate/{id}")
    public ResponseEntity<String> simulateRide(@PathVariable Integer id){
        rideService.simulate(id);
        return new ResponseEntity<>("Simulating ...",HttpStatus.OK);
    }




}
