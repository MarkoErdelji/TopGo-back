package com.example.topgoback.Vehicles.Service;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Model.VehicleType;
import com.example.topgoback.Vehicles.Repository.VehicleRepository;
import com.example.topgoback.Vehicles.Repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    GeoLocationRepository geoLocationRepository;

    public String changeCurrentLocation(Integer id, GeoLocationDTO location) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        if (vehicle.isEmpty()) throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle does not exist!");
        if (vehicle.get().getDriver().equals(null)) throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Vehicle is not assigned to the specific driver!");
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLongitude(location.getLongitude());
        geoLocation.setLatitude(location.getLatitude());
        geoLocation.setAddress(location.getAddress());
        geoLocationRepository.save(geoLocation);

        vehicle.get().setCurrentLocation(geoLocation);
        vehicleRepository.save(vehicle.get());

        return "Coordinates successfully updated";
    }


    public float getTypePrice(Integer id) {
        VehicleType vt = vehicleTypeRepository.findById(id).orElse(null);
        return vt.getPriceByKm();
    }
}
