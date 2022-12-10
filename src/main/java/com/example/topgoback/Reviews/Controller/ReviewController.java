package com.example.topgoback.Reviews.Controller;

import com.example.topgoback.Reviews.DTO.*;
import com.example.topgoback.Reviews.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/review/")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @PostMapping(consumes = "application/json",value = "{rideId}/vehicle/{id}")
    public ResponseEntity<CreateReviewResponseDTO> addVehicleReview(@PathVariable(name="rideId") Integer rideId,
                                                                    @PathVariable(name="id") Integer id,
                                                                    @RequestBody CreateReviewDTO createReviewDTO) {


        CreateReviewResponseDTO createReviewResponseDTO = reviewService.addVehicleReview(rideId,id,createReviewDTO);
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "vehicle/{id}")
    public ResponseEntity<VehicleReviewListDTO> getVehicleReviews(@PathVariable(name="id") Integer id) {


        VehicleReviewListDTO vehicleReviewListDTO = reviewService.getVehicleReviews(id);
        return new ResponseEntity<>(vehicleReviewListDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json",value = "{rideId}/driver/{id}")
    public ResponseEntity<CreateReviewResponseDTO> addDriverReview(@PathVariable(name="rideId") Integer rideId,
                                                                   @PathVariable(name="id") Integer id,
                                                                   @RequestBody CreateReviewDTO createReviewDTO) {


        CreateReviewResponseDTO createReviewResponseDTO = reviewService.addDriverReview(rideId,id,createReviewDTO);
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "driver/{id}")
    public ResponseEntity<DriverReviewListDTO> getDriverReviews(@PathVariable(name="id") Integer id) {


        DriverReviewListDTO driverReviewListDTO = reviewService.getDriverReviews(id);
        return new ResponseEntity<>(driverReviewListDTO, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<List<RideReviewsDTO>> getRideReviews(@PathVariable(name="id") Integer id) {

        List<RideReviewsDTO> rideReviews = reviewService.getRideReviews(id);
        return new ResponseEntity<>(rideReviews, HttpStatus.OK);
    }

}