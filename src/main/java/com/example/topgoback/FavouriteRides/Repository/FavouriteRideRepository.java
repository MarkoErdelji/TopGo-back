package com.example.topgoback.FavouriteRides.Repository;

import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavouriteRideRepository extends JpaRepository<FavouriteRide,Integer> {
    @Query("SELECT fr FROM FavouriteRide fr JOIN fr.passengers p WHERE p.id = :passengerId")
    List<FavouriteRide> findByPassengerId(@Param("passengerId") Integer passengerId);
}
