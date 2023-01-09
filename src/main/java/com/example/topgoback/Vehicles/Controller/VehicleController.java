package com.example.topgoback.Vehicles.Controller;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.Vehicles.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;
    @PutMapping(consumes = "application/json",value = "{id}/location")
    public ResponseEntity<String> changeCurrentLocation(@PathVariable Integer id,
                                                        @RequestBody GeoLocationDTO location) {

        String response = vehicleService.changeCurrentLocation(id,location);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "type/{id}")
    public ResponseEntity<Float> getVehicleType(@PathVariable Integer id) {

        float response = vehicleService.getTypePrice(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
