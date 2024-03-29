package com.example.topgoback.Reviews.DTO;

import com.example.topgoback.Enums.ReviewType;
import com.example.topgoback.Reviews.Model.Review;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.User;

import java.util.ArrayList;
import java.util.List;

public class CreateReviewResponseDTO {
        int id;
        Float rating;
        String comment;
        UserRef passenger;

        ReviewType type;

    public CreateReviewResponseDTO() {
    }

    public CreateReviewResponseDTO(Review r){
        this.id = r.getId();
        this.rating = r.getRating();
        this.passenger = new UserRef(r.getPassenger());
        this.comment = r.getComment();
        this.type = r.getReviewType();
    }


    public ReviewType getType() {
        return type;
    }

    public void setType(ReviewType type) {
        this.type = type;
    }

    public static List<CreateReviewResponseDTO> convertToCreateReviewResponseDTO(List<Review> content) {
        List<CreateReviewResponseDTO> createReviewResponseDTOS = new ArrayList<CreateReviewResponseDTO>();
        for(Review r:content){
            createReviewResponseDTOS.add(new CreateReviewResponseDTO(r));
        }
        return createReviewResponseDTOS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public UserRef getPassenger() {
        return passenger;
    }

    public void setPassenger(UserRef passenger) {
        this.passenger = passenger;
    }
}
