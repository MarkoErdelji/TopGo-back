package com.example.topgoback.Reviews.Service;

import com.example.topgoback.Enums.ReviewType;
import com.example.topgoback.Reviews.DTO.*;
import com.example.topgoback.Reviews.Model.Review;
import com.example.topgoback.Reviews.Repository.ReviewRepository;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.UserListDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Repository.UserRepository;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public CreateReviewResponseDTO addVehicleReview(int rideId, CreateReviewDTO createReviewDTO,String authorization) {
        Optional<Ride> ride = rideRepository.findById(rideId);
        if(ride.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ride does not exist!");
        }
        Review review = new Review();
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        Optional<Passenger> passenger = passengerRepository.findById(userId);
        if(passenger.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Passenger does not exist!");
        }
        Optional<Review> alreadyReviewed = reviewRepository.findByRideIdAndPassengerIdAndReviewType(rideId,userId,ReviewType.VEHICLE);
        if(alreadyReviewed.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Passenger already reviewed this driver");
        }
        review.setReviewType(ReviewType.VEHICLE);
        review.setRide(ride.get());
        review.setComment(createReviewDTO.getComment());
        review.setRating(createReviewDTO.getRating());
        review.setPassenger(passenger.get());
        reviewRepository.save(review);

        CreateReviewResponseDTO createReviewResponseDTO = new CreateReviewResponseDTO(review);
        return createReviewResponseDTO;
    }

    public VehicleReviewListDTO getVehicleReviews(int vehicleId, Pageable pageable) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if(vehicle.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle does not exist!");
        }
        Page<Review> page = reviewRepository.findByRideDriverVehicleId(vehicleId,pageable);
        List<CreateReviewResponseDTO> createReviewResponseDTOS = CreateReviewResponseDTO.convertToCreateReviewResponseDTO(page.getContent());
        VehicleReviewListDTO vehicleReviewListDTO = new VehicleReviewListDTO(new PageImpl<>(createReviewResponseDTOS, pageable, page.getTotalElements()));

        return vehicleReviewListDTO;
    }


    public CreateReviewResponseDTO addDriverReview(int rideId, CreateReviewDTO createReviewDTO,String authorization) {
        Optional<Ride> ride = rideRepository.findById(rideId);
        Review review = new Review();
        if(ride.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ride does not exist!");
        }
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        Optional<Passenger> passenger = passengerRepository.findById(userId);
        if(passenger.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Passenger does not exist!");
        }
        Optional<Review> alreadyReviewed = reviewRepository.findByRideIdAndPassengerIdAndReviewType(rideId,userId,ReviewType.DRIVER);
        if(alreadyReviewed.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Passenger already reviewed this driver");
        }
        review.setReviewType(ReviewType.DRIVER);
        review.setRide(ride.get());
        review.setComment(createReviewDTO.getComment());
        review.setRating(createReviewDTO.getRating());
        review.setPassenger(passenger.get());
        reviewRepository.save(review);
        return new CreateReviewResponseDTO(review);
    }

    public DriverReviewListDTO getDriverReviews(int driverId,Pageable pageable) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Driver does not exist!");
        }
        Page<Review> page = reviewRepository.findByRideDriverId(driverId,pageable);
        List<CreateReviewResponseDTO> createReviewResponseDTOS = CreateReviewResponseDTO.convertToCreateReviewResponseDTO(page.getContent());
        DriverReviewListDTO driverReviewListDTO = new DriverReviewListDTO(new PageImpl<>(createReviewResponseDTOS, pageable, page.getTotalElements()));
        return driverReviewListDTO;
    }

    public List<CreateReviewResponseDTO> getAllRideReviews(Integer id){
        List<Review> reviews = reviewRepository.findByRideId(id);
        List<CreateReviewResponseDTO> reviewsDTOS = new ArrayList<CreateReviewResponseDTO>();
        for(Review review : reviews){
            reviewsDTOS.add(new CreateReviewResponseDTO(review));
        }
        return reviewsDTOS;
    }

    public List<RideReviewsDTO> getRideReviews(Integer id,String authorization) {

        RideReviewsDTO rideReviewsDTO = new RideReviewsDTO();
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int passengerId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        Optional<Review> passengerVehicleReview = reviewRepository.findByRideIdAndPassengerIdAndReviewType(id,passengerId,ReviewType.VEHICLE);
        Optional<Review> passengerDriverReview = reviewRepository.findByRideIdAndPassengerIdAndReviewType(id,passengerId,ReviewType.DRIVER);

        if(passengerVehicleReview.isEmpty()){
            rideReviewsDTO.setVehicleReview(new CreateReviewResponseDTO());
        }
        else{
            rideReviewsDTO.setVehicleReview(new CreateReviewResponseDTO(passengerVehicleReview.get()));
        }
        if(passengerDriverReview.isEmpty()){
            rideReviewsDTO.setDriverReview(new CreateReviewResponseDTO());
        }
        else{
            rideReviewsDTO.setDriverReview(new CreateReviewResponseDTO(passengerDriverReview.get()));
        }

        List<RideReviewsDTO> rideReviewsDTOS = new ArrayList<>();
        rideReviewsDTOS.add(rideReviewsDTO);

        return rideReviewsDTOS;
    }
}
