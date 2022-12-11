package com.example.topgoback.Reviews.DTO;

public class RideReviewsDTO {
    CreateReviewResponseDTO vehicleReview;
    CreateReviewResponseDTO driverReview;

    public RideReviewsDTO() {
    }

    public CreateReviewResponseDTO getVehicleReview() {
        return vehicleReview;
    }

    public void setVehicleReview(CreateReviewResponseDTO vehicleReview) {
        this.vehicleReview = vehicleReview;
    }

    public CreateReviewResponseDTO getDriverReview() {
        return driverReview;
    }

    public void setDriverReview(CreateReviewResponseDTO driverReview) {
        this.driverReview = driverReview;
    }
}
