package com.example.topgoback.GeoLocations.Service;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService {
    @Autowired
    private GeoLocationRepository geoLocationRepository;
    public void addOne(GeoLocationDTO geoLocationDTO)
    {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress(geoLocationDTO.getAddress());
        geoLocation.setLatitude(geoLocationDTO.getLatitude());
        geoLocation.setLongitude(geoLocationDTO.getLongitude());

        geoLocationRepository.save(geoLocation);
    }
    public GeoLocation findById(int id){
        return geoLocationRepository.findById(id).orElseGet(null);
    }
}
