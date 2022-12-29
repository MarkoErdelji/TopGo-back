package com.example.topgoback.Vehicles.Service;
import com.example.topgoback.Vehicles.Model.VehicleType;
import com.example.topgoback.Vehicles.Repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleTypeService {
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    public VehicleType findById(Integer id){
        return this.vehicleTypeRepository.findById(id).orElseGet(null);
    }
}
