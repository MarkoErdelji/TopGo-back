package com.example.topgoback.Users.Model;

import com.example.topgoback.Enums.UserType;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.Payments.Model.Payment;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Routes.Model.Route;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Passenger extends  User{
    @OneToMany(mappedBy = "passenger", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Payment> payments;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH},fetch = FetchType.EAGER)
    @JoinTable(name = "passenger_rides", joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"))
    private List<Ride> rides;
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "passenger_favourite_rides", joinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "favourite_ride_id", referencedColumnName = "id"))
    private List<FavouriteRide> favouriteRoutes;

    @Column(name = "is_active")
    private boolean isActive;

    public Passenger() {

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


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
