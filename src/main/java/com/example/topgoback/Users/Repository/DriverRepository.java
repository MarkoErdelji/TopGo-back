package com.example.topgoback.Users.Repository;

import com.example.topgoback.Users.Model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver,Integer> {
    List<Driver> findByIsActiveTrue();
    Driver findByEmail(String email);
}
