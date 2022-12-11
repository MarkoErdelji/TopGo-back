package com.example.topgoback.GeoLocations.Model;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GeoLocation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String address;
    private float latitude;
    private float longitude;

    public GeoLocation() {
    }

    public GeoLocation(String address, float latitude, float longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoLocation(GeoLocationDTO destination) {
        this.address = destination.getAddress();
        this.latitude = destination.getLatitude();
        this.longitude = destination.getLongitude();
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
