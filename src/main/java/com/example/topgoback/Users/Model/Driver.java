package com.example.topgoback.Users.Model;

import com.example.topgoback.Documents.Model.Document;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Vehicles.Model.Vehicle;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name="Drivers")
public class Driver extends User{

    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Document> documents;
    @Column(name="isActive", unique=false, nullable=true)
    private boolean isActive;
    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ride> rides;
    @OneToOne
    @JoinColumn(name="vehicle_id", unique=false, nullable=true)
    private Vehicle vehicle;

    public Driver() {
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}

