package com.example.topgoback.Reviews.DTO;

import com.example.topgoback.Users.DTO.UserRef;

public class CreateReviewResponseDTO {
    Integer id;
    Integer rating;
    String comment;
    UserRef passenger;

    public CreateReviewResponseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserRef getPassenger() {
        return passenger;
    }

    public void setPassenger(UserRef passenger) {
        this.passenger = passenger;
    }
}
