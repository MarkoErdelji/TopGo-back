package com.example.topgoback.GeoLocations.DTO;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import jakarta.validation.constraints.NotNull;

public class GeoLocationDTO {

    @NotNull(message = "is required!")
    private String address;
    @NotNull(message = "is required!")
    private float latitude;
    @NotNull(message = "is required!")
    private float longitude;

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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
