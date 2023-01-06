package com.example.topgoback.Routes.DTO;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;

public class RouteForCreateRideDTO {
    private GeoLocationDTO departure;
    private GeoLocationDTO destination;

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
