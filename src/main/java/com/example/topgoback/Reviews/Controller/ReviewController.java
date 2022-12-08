package com.example.topgoback.Reviews.Controller;

import com.example.topgoback.Reviews.DTO.CreateReviewDTO;
import com.example.topgoback.Reviews.DTO.CreateReviewResponseDTO;
import com.example.topgoback.Reviews.Service.ReviewService;
import com.example.topgoback.Users.DTO.UnregisteredUserAssumptionDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserDTO;
import com.example.topgoback.Users.Service.UserService;
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
    public ResponseEntity<CreateReviewResponseDTO> getAssumption(@PathVariable(name="rideId") Integer rideId,
                                                                    @PathVariable(name="id") Integer id,
                                                                    @RequestBody CreateReviewDTO createReviewDTO) {


        CreateReviewResponseDTO createReviewResponseDTO = reviewService.addOne(rideId,id,createReviewDTO);
        return new ResponseEntity<>(createReviewResponseDTO, HttpStatus.CREATED);
    }

}