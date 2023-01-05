package com.example.topgoback.Rides.Repository;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface RideRepository extends JpaRepository<Ride,Integer> {
    @Query("SELECT DISTINCT r FROM Ride r INNER JOIN r.passenger p WHERE (r.driver.id = :userId OR p.id = :userId) AND (r.start BETWEEN :startDate AND :endDate)")
    Page<Ride> findByDriverOrPassengerAndBeginBetween(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}