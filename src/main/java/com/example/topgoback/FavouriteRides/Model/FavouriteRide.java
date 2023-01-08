package com.example.topgoback.FavouriteRides.Model;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class FavouriteRide {
    @Id
    @SequenceGenerator(name = "mySeqGenUser", sequenceName = "mySeqGenUser", initialValue = 7, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenUser")
    @Column(name="id")
    private Integer id;
    public String favoriteName;

    @OneToOne
    @JoinColumn(name="route_id", unique=false, nullable=true)

    public Route route;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "passenger_favourite_rides", joinColumns = @JoinColumn(name = "favourite_ride_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    public List<Passenger> passengers;

    public String vehicleType;

    public boolean babyTransport;

    public boolean petTransport;

    public FavouriteRide() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }
}
