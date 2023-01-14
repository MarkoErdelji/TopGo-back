package com.example.topgoback.Routes.Model;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Tools.DistanceCalculator;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Route {
    @Id
    @SequenceGenerator(name = "mySeqGenRoute", sequenceName = "mySeqGenRoute", initialValue = 7, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenRoute")
    private int id;

    @ManyToOne(optional=false)
    @JoinColumn(name="start_id", unique=false, nullable=false)
    private GeoLocation start;
    @ManyToOne(optional=false)
    @JoinColumn(name="finish_id", unique=false, nullable=false)
    private GeoLocation finish;
    @Column(name = "lenght")
    private float lenght;


    public Route(GeoLocation start, GeoLocation finish) {
        this.start = start;
        this.finish = finish;
        this.lenght = lenght;
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


}
