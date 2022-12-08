package com.example.topgoback.Reviews.Controller;

import com.example.topgoback.Reviews.DTO.CreateReviewDTO;
import com.example.topgoback.Reviews.DTO.CreateReviewResponseDTO;
import com.example.topgoback.Reviews.DTO.VehicleReviewListDTO;
import com.example.topgoback.Reviews.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "vehicle/{id}")
    public ResponseEntity<VehicleReviewListDTO> getVehicleReviews(@PathVariable(name="id") Integer id) {


        VehicleReviewListDTO vehicleReviewListDTO = reviewService.getVehicleReviews(id);
        return new ResponseEntity<>(vehicleReviewListDTO, HttpStatus.CREATED);
    }

    @PostMapping(consumes = "application/json",value = "{rideId}/driver/{id}")
    public ResponseEntity<CreateReviewResponseDTO> addDriverReview(@PathVariable(name="rideId") Integer rideId,
                                                                   @PathVariable(name="id") Integer id,
                                                                   @RequestBody CreateReviewDTO createReviewDTO) {


        CreateReviewResponseDTO createReviewResponseDTO = reviewService.addDriverReview(rideId,id,createReviewDTO);
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.CREATED);
    }

}