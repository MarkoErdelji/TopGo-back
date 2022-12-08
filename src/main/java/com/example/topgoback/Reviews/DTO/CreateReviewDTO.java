package com.example.topgoback.Reviews.DTO;

public class CreateReviewDTO {
    Integer rating;
    String comment;

    public CreateReviewDTO() {
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
}
