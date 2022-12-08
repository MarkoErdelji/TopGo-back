package com.example.topgoback.Reviews.Service;

import com.example.topgoback.Reviews.DTO.*;
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

    public DriverReviewListDTO getDriverReviews(int driverId) {

        CreateReviewResponseDTO createReviewResponseDTO = new CreateReviewResponseDTO();
        createReviewResponseDTO.setId(123);
        createReviewResponseDTO.setRating(3);
        createReviewResponseDTO.setComment("The driver was driving too fast");
        createReviewResponseDTO.setPassenger(UserRef.getMockupData());

        List<CreateReviewResponseDTO> reviewResponseDTOList = new ArrayList<>();
        reviewResponseDTOList.add(createReviewResponseDTO);

        DriverReviewListDTO driverReviewListDTO = new DriverReviewListDTO();
        driverReviewListDTO.setTotalCount(243);
        driverReviewListDTO.setResults(reviewResponseDTOList);

        return driverReviewListDTO;
    }

    public List<RideReviewsDTO> getRideReviews(Integer id) {
        RideReviewsDTO rideReviewsDTO = new RideReviewsDTO();

        CreateReviewResponseDTO vehicleReviewResponseDTO = new CreateReviewResponseDTO();
        vehicleReviewResponseDTO.setId(123);
        vehicleReviewResponseDTO.setRating(3);
        vehicleReviewResponseDTO.setComment("The vehicle was bad and dirty");
        vehicleReviewResponseDTO.setPassenger(UserRef.getMockupData());

        CreateReviewResponseDTO driverReviewResponseDTO = new CreateReviewResponseDTO();
        driverReviewResponseDTO.setId(123);
        driverReviewResponseDTO.setRating(3);
        driverReviewResponseDTO.setComment("The driver was driving too fast");
        driverReviewResponseDTO.setPassenger(UserRef.getMockupData());

        rideReviewsDTO.setVehicleReview(vehicleReviewResponseDTO);
        rideReviewsDTO.setDriverReview(driverReviewResponseDTO);

        List<RideReviewsDTO> rideReviewsDTOS = new ArrayList<>();
        rideReviewsDTOS.add(rideReviewsDTO);

        return rideReviewsDTOS;
    }
}
