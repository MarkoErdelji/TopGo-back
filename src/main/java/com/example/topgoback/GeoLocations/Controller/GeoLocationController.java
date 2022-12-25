package com.example.topgoback.GeoLocations.Controller;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Service.GeoLocationService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/geoLocation")
@CrossOrigin(origins = "http://localhost:4200")
public class GeoLocationController {
    @Autowired
    private GeoLocationService geoLocationService;

    @PostMapping(consumes = "Application/json")
    public ResponseEntity<GeoLocationDTO> saveGeoLocation(@RequestBody GeoLocationDTO geoLocationDTO){
        geoLocationService.addOne(geoLocationDTO);

        return new ResponseEntity<>(geoLocationDTO, HttpStatus.OK);

    }

}
