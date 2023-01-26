package com.example.topgoback.GeoLocations.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class DepartureDestinationDTO {

    @Valid
    @NotNull(message = "is required!")
    GeoLocationDTO departure;


    @Valid
    @NotNull(message = "is required!")
    GeoLocationDTO destination;

    public static DepartureDestinationDTO getMockedData(){
        DepartureDestinationDTO departureDestinationDTO = new DepartureDestinationDTO();

        GeoLocationDTO geoLocationDTO = new GeoLocationDTO();

        geoLocationDTO.setAddress("Bulevar oslobodjenja 46");
        geoLocationDTO.setLatitude(45.267136F);
        geoLocationDTO.setLongitude(19.833549F);
        departureDestinationDTO.setDeparture(geoLocationDTO);
        departureDestinationDTO.setDestination(geoLocationDTO);

        return departureDestinationDTO;
    }
    public DepartureDestinationDTO() {
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
