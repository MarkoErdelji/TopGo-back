package com.example.topgoback.Users.Model;

import com.example.topgoback.Payments.Model.Payment;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Routes.Model.Route;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Passenger extends  User{
    @OneToMany(mappedBy = "passenger", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Payment> payments;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "passenger_rides", joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"))
    private List<Ride> rides;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "favourite_routes", joinColumns = @JoinColumn(name = "route_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "passanger_id", referencedColumnName = "id"))
    private List<Route> favouriteRoutes;

    public Passenger() {

    }

    public Passenger(String firstName, String lastName, String email, String password, String phoneNumber, String address, List<Payment> payments, List<Ride> rides, List<Route> favouriteRoutes) {
        super(firstName, lastName, email, password, phoneNumber, address);
        this.payments = payments;
        this.rides = rides;
        this.favouriteRoutes = favouriteRoutes;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public List<Route> getFavouriteRoutes() {
        return favouriteRoutes;
    }

    public void setFavouriteRoutes(List<Route> favouriteRoutes) {
        this.favouriteRoutes = favouriteRoutes;
    }
}
