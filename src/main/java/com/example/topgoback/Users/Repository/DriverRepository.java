package com.example.topgoback.Users.Repository;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Integer> {
    List<Driver> findByIsActiveTrue();
    Driver findByEmail(String email);

}
