package com.example.topgoback.Reviews.Model;

import com.example.topgoback.Enums.ReviewType;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.persistence.*;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "rating")
    private float rating;
    @Column(name = "comment")
    private String comment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ride_id")
    private Ride ride ;

    @Column(name="review_type")
    private ReviewType reviewType;

    public Review() {
    }



    public Review(int id, float rating, String comment, Passenger passenger, Ride ride, ReviewType reviewType) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.passenger = passenger;
        this.ride = ride;
        this.reviewType = reviewType;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
