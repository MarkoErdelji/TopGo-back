package com.example.topgoback.Vehicles.Repository;

import com.example.topgoback.Vehicles.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {
}
