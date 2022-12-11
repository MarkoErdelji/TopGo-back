package com.example.topgoback.Rides.Repository;

import com.example.topgoback.Rides.Model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride,Integer> {
}
