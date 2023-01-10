package com.example.topgoback.Reviews.Controller;

import com.example.topgoback.Reviews.DTO.*;
import com.example.topgoback.Reviews.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/review/")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @PostMapping(consumes = "application/json",value = "{rideId}/vehicle")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CreateReviewResponseDTO> addVehicleReview(@PathVariable(name="rideId") Integer rideId,
                                                                    @RequestBody CreateReviewDTO createReviewDTO,
                                                                    @RequestHeader("Authorization") String authorization) {


        CreateReviewResponseDTO createReviewResponseDTO = reviewService.addVehicleReview(rideId,createReviewDTO,authorization);
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "vehicle/{id}")
    public ResponseEntity<VehicleReviewListDTO> getVehicleReviews(@PathVariable(name="id") Integer id,
                                                                  @Valid @RequestParam(required = false,defaultValue = "0") Integer page,
                                                                  @Valid  @RequestParam(required = false,defaultValue =  "0") Integer size,
                                                                  Pageable pageable)
    {

        if (size == 0 || size == Pageable.unpaged().getPageSize()) {
            pageable = PageRequest.of(pageable.getPageNumber(), Integer.MAX_VALUE, pageable.getSort());
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }
        VehicleReviewListDTO vehicleReviewListDTO = reviewService.getVehicleReviews(id,pageable);
        return new ResponseEntity<>(vehicleReviewListDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json",value = "{rideId}/driver")
    public ResponseEntity<CreateReviewResponseDTO> addDriverReview(@PathVariable(name="rideId") Integer rideId,
                                                                   @RequestBody CreateReviewDTO createReviewDTO,
                                                                   @RequestHeader("Authorization") String authorization) {


        CreateReviewResponseDTO createReviewResponseDTO = reviewService.addDriverReview(rideId,createReviewDTO,authorization);
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "driver/{id}")
    public ResponseEntity<DriverReviewListDTO> getDriverReviews(@PathVariable(name="id") Integer id,
                                                                @Valid @RequestParam(required = false,defaultValue = "0") Integer page,
                                                                @Valid  @RequestParam(required = false,defaultValue =  "0") Integer size,
                                                                Pageable pageable) {

        if (size == 0 || size == Pageable.unpaged().getPageSize()) {
            pageable = PageRequest.of(pageable.getPageNumber(), Integer.MAX_VALUE, pageable.getSort());
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }
        DriverReviewListDTO driverReviewListDTO = reviewService.getDriverReviews(id,pageable);
        return new ResponseEntity<>(driverReviewListDTO, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<List<RideReviewsDTO>> getRideReviews(@PathVariable(name="id") Integer id,@RequestHeader("Authorization") String authorization) {

        List<RideReviewsDTO> rideReviews = reviewService.getRideReviews(id,authorization);
        return new ResponseEntity<>(rideReviews, HttpStatus.OK);
    }

}