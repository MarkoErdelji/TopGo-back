package com.example.topgoback.Reviews.Repository;

import com.example.topgoback.Reviews.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
}
