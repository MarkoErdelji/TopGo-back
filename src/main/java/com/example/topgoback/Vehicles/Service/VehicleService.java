package com.example.topgoback.Vehicles.Service;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {


    public GeoLocationDTO changeCurrentLocation(Integer id) {
        GeoLocationDTO response = new GeoLocationDTO();
        response.setLongitude(19.833549f);
        response.setLatitude(45.267136f);
        response.setAddress("Bulevar oslobodjenja 46");
        return response;
    }
}
