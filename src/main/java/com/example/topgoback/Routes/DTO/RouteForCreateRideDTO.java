package com.example.topgoback.Routes.DTO;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;

public class RouteForCreateRideDTO {
    private GeoLocationDTO departure;
    private GeoLocationDTO destinations;

    public GeoLocationDTO getDeparture() {
        return departure;
    }

    public void setDeparture(GeoLocationDTO departure) {
        this.departure = departure;
    }

    public GeoLocationDTO getDestinations() {
        return destinations;
    }

    public void setDestinations(GeoLocationDTO destinations) {
        this.destinations = destinations;
    }
}
