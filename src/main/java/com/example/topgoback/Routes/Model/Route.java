package com.example.topgoback.Routes.Model;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import jakarta.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional=false)
    @JoinColumn(name="start_id", unique=false, nullable=false)
    private GeoLocation start;
    @ManyToOne(optional=false)
    @JoinColumn(name="finish_id", unique=false, nullable=false)
    private GeoLocation finish;
    @Column(name = "lenght")
    private float lenght;
    @Column(name = "estunatedTime")
    private float estimatedTime;
    @Column(name = "price")
    private float price;


    public Route(GeoLocation start, GeoLocation finish) {
        this.start = start;
        this.finish = finish;
        this.lenght = lenght;
        this.estimatedTime = estimatedTime;
        this.price = price;
    }

    public Route() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GeoLocation getStart() {
        return start;
    }

    public void setStart(GeoLocation start) {
        this.start = start;
    }

    public GeoLocation getFinish() {
        return finish;
    }

    public void setFinish(GeoLocation finish) {
        this.finish = finish;
    }

    public float getLenght() {
        return lenght;
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
    }

    public float getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(float estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
