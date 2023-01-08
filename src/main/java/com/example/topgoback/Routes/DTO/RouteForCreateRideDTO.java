package com.example.topgoback.Routes.DTO;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Routes.Model.Route;

public class RouteForCreateRideDTO {
    private GeoLocationDTO departure;
    private GeoLocationDTO destination;


    public RouteForCreateRideDTO(){

    }
    public RouteForCreateRideDTO(Route route) {
        this.departure = new GeoLocationDTO(route.getStart());
        this.destination = new GeoLocationDTO(route.getFinish());

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
