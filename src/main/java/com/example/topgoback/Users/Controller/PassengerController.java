package com.example.topgoback.Users.Controller;

import com.example.topgoback.AccountActivationToken.Service.AccountActivationTokenService;
import com.example.topgoback.Enums.AllowedSortFields;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Tools.JwtCheckAnnotation;
import com.example.topgoback.Users.DTO.*;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Service.PassengerService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public ResponseEntity<?> create(@Valid @RequestBody CreatePassengerDTO createPassengerDTO) throws MessagingException, IOException {

    Passenger passenger = passengerService.addOne(createPassengerDTO);
    return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);


    }
    @GetMapping(value = "/{id}")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('USER')")
    public  ResponseEntity<CreatePassengerResponseDTO> getOne(@PathVariable(value = "id") Integer id){
        Passenger passenger = passengerService.findById(id);

        return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);

    }
    @PutMapping(value = "/{id}", consumes = "application/json")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('USER')")
    public ResponseEntity<CreatePassengerResponseDTO> updateOne(@PathVariable Integer id, @Valid @RequestBody UpdatePassengerDTO createPassengerDTO){
        Passenger passenger = passengerService.update(createPassengerDTO, id);

        return new ResponseEntity<>(new CreatePassengerResponseDTO(passenger), HttpStatus.OK);
    }
    @GetMapping
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN')")
    public  ResponseEntity<?> getPaginated(@RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size,
                                           Pageable pageable){

        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        PassengerListDTO passengers = passengerService.findAll(pageable);
        return new ResponseEntity<>(passengers, HttpStatus.OK);

    }

    @GetMapping(value = "/activate/{activationId}")
    @Valid
    public ResponseEntity<?> getActivation(@PathVariable int activationId){
        activationTokenService.findAndActivate(activationId);
        return new ResponseEntity<>("Succesfull account activation", HttpStatus.OK);
    }

    @GetMapping(value = "{id}/ride")
    @Valid
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('USER')")
    public ResponseEntity<?> getRides(@PathVariable Integer id,
                                      @RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime beginDateInterval,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateInterval,
                                      Pageable pageable)
    {

        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        if(sort == null){
            sort = "id";
        }
        else{
            boolean isValidSortField = false;
            for (AllowedSortFields allowedField : AllowedSortFields.values()) {
                if (sort.equals(allowedField.getField())) {
                    isValidSortField = true;
                    break;
                }
            }
            if (!isValidSortField) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid sort field. Allowed fields: " + Arrays.toString(AllowedSortFields.values()));
            }
        }
        if(beginDateInterval == null){
            beginDateInterval = LocalDateTime.of(0001, 01, 01, 00, 00, 00, 00);;
        }
        if(endDateInterval == null){
            endDateInterval = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999);
        }
        pageable = (Pageable) PageRequest.of(page, size, Sort.by(sort).ascending());

        UserRidesListDTO rides = rideService.findRidesByPassengerId(id,pageable,beginDateInterval,endDateInterval);


        if(rides == null){
            return new ResponseEntity<>("User has no rides!",HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(rides, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/ride/finished")
    @Valid
    public ResponseEntity<List<RideDTO>> getPassengerFinishedRides(@RequestHeader("Authorization") String authorization){
        List<RideDTO> response = passengerService.getPassengerFinishedRides(authorization);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/ride/invite/{id}")
    @Valid
    public ResponseEntity<InviteFriendDTO> invitePassengerForRide(@RequestHeader("Authorization") String authorization
    ,@PathVariable int id){
        InviteFriendDTO response = passengerService.inviteFriend(authorization,id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(value = "/ride/response",consumes = "application/json")
    @Valid
    public ResponseEntity<InviteFriendDTO> inviteReponse(@RequestBody InviteFriendDTO res){
        InviteFriendDTO response = passengerService.inviteResponse(res);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }




}
