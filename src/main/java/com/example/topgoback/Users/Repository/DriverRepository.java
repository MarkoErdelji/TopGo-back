package com.example.topgoback.Users.Repository;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver,Integer> {
    List<Driver> findByIsActiveTrue();
    Page<Driver> findAll(Pageable pageable);
    Driver findByEmail(String email);

}
