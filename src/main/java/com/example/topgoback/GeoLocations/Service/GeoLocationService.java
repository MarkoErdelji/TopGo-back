package com.example.topgoback.GeoLocations.Service;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService {
    @Autowired
    private GeoLocationRepository geoLocationRepository;
    public void addOne(GeoLocation geoLocation)
    {
        geoLocationRepository.save(geoLocation);
    }
}
