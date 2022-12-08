package com.example.topgoback.Reviews.Service;

import com.example.topgoback.Reviews.DTO.CreateReviewDTO;
import com.example.topgoback.Reviews.DTO.CreateReviewResponseDTO;
import com.example.topgoback.Reviews.Repository.ReviewRepository;
import com.example.topgoback.Users.DTO.UserRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;


    public CreateReviewResponseDTO addOne(int rideId, int id, CreateReviewDTO createReviewDTO) {
        CreateReviewResponseDTO createReviewResponseDTO = new CreateReviewResponseDTO();
        createReviewResponseDTO.setId(123);
        createReviewResponseDTO.setRating(3);
        createReviewResponseDTO.setComment("The driver was driving really fast");
        createReviewResponseDTO.setPassenger(UserRef.getMockupData());
        return createReviewResponseDTO;
    }
}
