package com.example.topgoback.Vehicles.Service;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Service.GeoLocationService;
import com.example.topgoback.Vehicles.DTO.VehicleDTO;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Repository.VehicleRepository;
import com.example.topgoback.Vehicles.Repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    private VehicleTypeService vehicleTypeService;

    private GeoLocationService geoLocationService;


    public GeoLocationDTO changeCurrentLocation(Integer id) {
        GeoLocationDTO response = new GeoLocationDTO();
        response.setLongitude(19.833549f);
        response.setLatitude(45.267136f);
        response.setAddress("Bulevar oslobodjenja 46");
        return response;
    }

    public VehicleDTO addOne(VehicleDTO vehicleDTO){
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(vehicleTypeService.findById(vehicleDTO.getTypeId()));

        return vehicleDTO;
    }
}
