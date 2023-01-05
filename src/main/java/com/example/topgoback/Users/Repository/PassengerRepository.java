package com.example.topgoback.Users.Repository;

import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Integer> {
    Page<Passenger> findAll(Pageable pageable);
    Passenger findByEmail(String username);
}
