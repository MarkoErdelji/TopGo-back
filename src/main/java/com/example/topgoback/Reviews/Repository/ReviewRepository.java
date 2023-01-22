package com.example.topgoback.Reviews.Repository;

import com.example.topgoback.Enums.ReviewType;
import com.example.topgoback.Reviews.Model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Page<Review> findByRideDriverVehicleIdAndReviewType(int vehicleId, Pageable pageable,ReviewType reviewType);

    Page<Review> findByRideDriverIdAndReviewType(int driverId, Pageable pageable,ReviewType reviewType);

    Optional<Review> findByPassengerIdAndReviewType(int passengerId,ReviewType reviewType);
    Optional<Review> findByRideIdAndPassengerIdAndReviewType(int rideId, int passengerId, ReviewType reviewType);
    List<Review> findByRideId(int rideId);


}
