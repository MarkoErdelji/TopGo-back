package com.example.topgoback.Routes.DTO;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Routes.Model.Route;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class RouteForCreateRideDTO {

    @Valid
    @NotNull
    private GeoLocationDTO departure;
    @Valid
    @NotNull
    private GeoLocationDTO destination;

    @Valid
    private float lenght;

    public RouteForCreateRideDTO(){

    }
    public RouteForCreateRideDTO(Route route) {
        this.departure = new GeoLocationDTO(route.getStart());
        this.destination = new GeoLocationDTO(route.getFinish());
        this.lenght = route.getLenght();
    }


    public float getLenght() {
        return lenght;
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
    }

    public GeoLocationDTO getDeparture() {
        return departure;
    }

    public void setDeparture(GeoLocationDTO departure) {
        this.departure = departure;
    }

    public GeoLocationDTO getDestination() {
        return destination;
    }

    public void setDestination(GeoLocationDTO destination) {
        this.destination = destination;
    }


}
