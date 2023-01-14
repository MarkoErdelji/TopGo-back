package com.example.topgoback.GeoLocations.Model;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import jakarta.persistence.*;

@Entity
public class GeoLocation {
    @Id
    @SequenceGenerator(name = "mySeqGenLocation", sequenceName = "mySeqGenLocation", initialValue = 590)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenLocation")
    private Integer id;
    @Column(name = "address", unique = false, nullable = true)
    private String address;
    @Column(name = "latitude", unique = false, nullable = true)
    private float latitude;
    @Column(name = "longitude", unique = false, nullable = true)
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
