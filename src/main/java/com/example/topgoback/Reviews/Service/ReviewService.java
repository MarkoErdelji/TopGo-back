package com.example.topgoback.Reviews.Service;

import com.example.topgoback.Reviews.DTO.CreateReviewDTO;
import com.example.topgoback.Reviews.DTO.CreateReviewResponseDTO;
import com.example.topgoback.Reviews.DTO.VehicleReviewListDTO;
import com.example.topgoback.Reviews.Repository.ReviewRepository;
import com.example.topgoback.Users.DTO.UserRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;


    public CreateReviewResponseDTO addVehicleReview(int rideId, int id, CreateReviewDTO createReviewDTO) {
        CreateReviewResponseDTO createReviewResponseDTO = new CreateReviewResponseDTO();
        createReviewResponseDTO.setId(123);
        createReviewResponseDTO.setRating(3);
        createReviewResponseDTO.setComment("The driver was driving really fast");
        createReviewResponseDTO.setPassenger(UserRef.getMockupData());
        return createReviewResponseDTO;
    }

    public VehicleReviewListDTO getVehicleReviews(int vehicleId) {

        CreateReviewResponseDTO createReviewResponseDTO = new CreateReviewResponseDTO();
        createReviewResponseDTO.setId(123);
        createReviewResponseDTO.setRating(3);
        createReviewResponseDTO.setComment("The driver was driving too fast");
        createReviewResponseDTO.setPassenger(UserRef.getMockupData());

        List<CreateReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();
        reviewResponseDTOList.add(createReviewResponseDTO);

        VehicleReviewListDTO vehicleReviewListDTO = new VehicleReviewListDTO();
        vehicleReviewListDTO.setTotalCount(243);
        vehicleReviewListDTO.setResults(reviewResponseDTOList);

        return vehicleReviewListDTO;
    }


    public CreateReviewResponseDTO addDriverReview(int rideId, int id, CreateReviewDTO createReviewDTO) {
        CreateReviewResponseDTO createReviewResponseDTO = new CreateReviewResponseDTO();
        createReviewResponseDTO.setId(123);
        createReviewResponseDTO.setRating(3);
        createReviewResponseDTO.setComment(createReviewDTO.getComment());
        createReviewResponseDTO.setPassenger(UserRef.getMockupData());
        return createReviewResponseDTO;
    }
}
