package com.example.topgoback.Users.Model;

import com.example.topgoback.Rides.Model.Ride;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name="Drivers")
public class Driver extends User{

    @Column(name="driversLicense", unique=false, nullable=true)
    private String driversLicense;
    @Column(name="isActive", unique=false, nullable=true)
    private boolean isActive;
    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ride> rides;

    public Driver() {
    }

    public Driver(String firstName, String lastName, String profilePicture, String email, String password, String phoneNumber, String address, String driversLicense, boolean isActive, List<Ride> rides) {
        super(firstName, lastName, profilePicture,email, password, phoneNumber, address);

        this.driversLicense = driversLicense;
        this.isActive = isActive;
        this.rides = rides;
    }

    public String getDriversLicense() {
        return driversLicense;
    }

    public void setDriversLicense(String driversLicense) {
        this.driversLicense = driversLicense;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
