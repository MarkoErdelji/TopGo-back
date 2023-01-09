package com.example.topgoback.Reviews.Repository;

import com.example.topgoback.Enums.ReviewType;
import com.example.topgoback.Reviews.Model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Page<Review> findByRideDriverVehicleId(int vehicleId, Pageable pageable);

    Page<Review> findByRideDriverId(int vehicleId, Pageable pageable);

    Optional<Review> findByPassengerIdAndReviewType(int passengerId,ReviewType reviewType);
    Optional<Review> findByRideIdAndPassengerIdAndReviewType(int rideId, int passengerId, ReviewType reviewType);


}
