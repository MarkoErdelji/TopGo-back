package com.example.topgoback.Users.DTO;

public class UnregisteredUserAssumptionDTO {

    private float assumedPrice;
    private float assumedTimeInMinutes;

    public float getAssumedPrice() {
        return assumedPrice;
    }

    public void setAssumedPrice(float assumedPrice) {
        this.assumedPrice = assumedPrice;
    }

    public float getAssumedTimeInMinutes() {
        return assumedTimeInMinutes;
    }

    public void setAssumedTimeInMinutes(float assumedTimeInMinutes) {
        this.assumedTimeInMinutes = assumedTimeInMinutes;
    }
}
