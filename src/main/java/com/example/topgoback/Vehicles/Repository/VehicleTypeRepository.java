package com.example.topgoback.Vehicles.Repository;


import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.Vehicles.Model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTypeRepository extends JpaRepository<VehicleType,Integer> {
    VehicleType findByVehicleName(VehicleName name);

}
