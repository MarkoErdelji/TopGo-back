package com.example.topgoback.Reviews.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class CreateReviewDTO {


    @DecimalMin(value = "0", message = "Rating must be at least 0")
    @DecimalMax(value = "5", message = "Rating must be no more than 5")
    @NotNull(message = "is required!")
    Float rating;

    @NotNull(message = "is required!")
    @Length(max=300,message= "cannot be longer than 300 characters!")
    String comment;

    public CreateReviewDTO() {
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
