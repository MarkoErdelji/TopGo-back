package com.example.topgoback.Users.DTO;

public class UnregisteredUserAssumptionDTO {

    private float estimatedCost;
    private float estimatedTimeInMinutes;

    public float getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(float assumedPrice) {
        this.estimatedCost = assumedPrice;
    }

    public float getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(float estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }
}
