package com.example.topgoback.Users.Repository;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Integer> {
    Page<Passenger> findAll(Pageable pageable);
    Passenger findByEmail(String username);

    @Query("SELECT DISTINCT r FROM Ride r JOIN r.passenger p WHERE (p.id = :passengerId) AND (r.status = 4)")
    List<Ride> findRidesByPassengerIdAndIsFinished(@Param("passengerId") int passengerId);
}
