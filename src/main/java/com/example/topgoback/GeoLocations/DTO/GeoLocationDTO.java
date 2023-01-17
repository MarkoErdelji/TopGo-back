package com.example.topgoback.GeoLocations.DTO;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import jakarta.validation.constraints.*;

import jakarta.validation.constraints.NotNull;

public class GeoLocationDTO {

    @NotNull(message = "is required!")
    private String address;
    @NotNull(message = "is required!")
    @Positive
    private Float latitude;
    @NotNull(message = "is required!")
    @Positive
    private Float longitude;

    public GeoLocationDTO(GeoLocation location) {
        this.address = location.getAddress();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public GeoLocationDTO() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
