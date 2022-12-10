package com.example.topgoback.Vehicles.Model;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Users.Model.Driver;
import jakarta.persistence.*;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name="driver_id", unique=false, nullable=true)
    private Driver driver;
    private String model;
    private String licencePlate;
    private int seatNumber;
    private boolean forBabies;
    private boolean forAnimals;
    @OneToOne
    @JoinColumn(name="location_id", unique=false, nullable=true)
    private GeoLocation currentLocation;
    @OneToOne
    @JoinColumn(name="type_id", unique=false, nullable=true)
    private VehicleType vehicleType;

    public Vehicle() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isForBabies() {
        return forBabies;
    }

    public void setForBabies(boolean forBabies) {
        this.forBabies = forBabies;
    }

    public boolean isForAnimals() {
        return forAnimals;
    }

    public void setForAnimals(boolean forAnimals) {
        this.forAnimals = forAnimals;
    }

    public GeoLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
