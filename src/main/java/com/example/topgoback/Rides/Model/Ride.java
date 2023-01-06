package com.example.topgoback.Rides.Model;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.Payments.Model.Payment;
import com.example.topgoback.RejectionLetters.Model.RejectionLetter;
import com.example.topgoback.Reviews.Model.Review;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.Passenger;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
public class Ride {
    @Id
    @SequenceGenerator(name = "mySeqGenRide", sequenceName = "mySeqGenRide", initialValue = 7, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenRide")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name = "passenger_rides", joinColumns = @JoinColumn(name = "ride_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    private List<Passenger> passenger;
    @Column(name = "startTime")
    private LocalDateTime start;
    @Column(name = "endTime")
    private LocalDateTime end;
    @Column(name = "price")
    private float price;
    @Column(name = "status")
    private Status status;
    @Column(name = "for_babies")
    private boolean forBabies;
    @Column(name = "for_animals")
    private boolean forAnimals;
    @Column(name = "panic")
    private boolean panic;
    @Column(name = "vehicle_name")
    private VehicleName vehicleName;
    @OneToMany(mappedBy = "ride", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToOne
    @JoinColumn(name="payment_id", unique=false, nullable=true)
    private Payment payment;

    @OneToOne
    @JoinColumn(name="rejection_id", unique=false, nullable=true)
    private RejectionLetter RejectionLetter;
    @OneToOne
    @JoinColumn(name="route_id", unique=false, nullable=true)
    private Route route;

    public Ride() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Passenger> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<Passenger> passenger) {
        this.passenger = passenger;
    }


    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public boolean isPanic() {
        return panic;
    }

    public void setPanic(boolean panic) {
        this.panic = panic;
    }

    public VehicleName getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(VehicleName vehicleName) {
        this.vehicleName = vehicleName;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public com.example.topgoback.RejectionLetters.Model.RejectionLetter getRejectionLetter() {
        return RejectionLetter;
    }

    public void setRejectionLetter(com.example.topgoback.RejectionLetters.Model.RejectionLetter rejectionLetter) {
        RejectionLetter = rejectionLetter;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
