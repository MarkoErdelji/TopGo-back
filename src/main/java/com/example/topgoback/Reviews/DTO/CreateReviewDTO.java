package com.example.topgoback.Reviews.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class CreateReviewDTO {


    @NotNull(message = "is required!")
    @Size(max = 5,min = 1,message="cannot be less than 1 or more than 5!")
    Integer rating;

    @NotNull(message = "is required!")
    @Length(max=300,message= "cannot be longer than 300 characters!")
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
