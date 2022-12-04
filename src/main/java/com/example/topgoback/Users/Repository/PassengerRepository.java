package com.example.topgoback.Users.Repository;

import com.example.topgoback.Users.Model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Integer> {
}
