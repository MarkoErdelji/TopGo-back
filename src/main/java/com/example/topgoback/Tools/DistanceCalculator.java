package com.example.topgoback.Tools;

import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Vehicles.Model.VehicleType;

public class DistanceCalculator {

    public static double getDistanceFromLocations(GeoLocationDTO startLocation, GeoLocationDTO endLocation){
        final int R = 6371; // Radious of the earth
        Double lat1 = (double) startLocation.getLatitude();
        Double lon1 = (double) startLocation.getLongitude();
        Double lat2 = (double) endLocation.getLatitude();
        Double lon2 = (double) endLocation.getLongitude();
        Double latDistance = (lat2-lat1) * Math.PI / 180;
        Double lonDistance = (lon2-lon1) * Math.PI / 180;
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return distance;
    }

    public static double getEstimatedTimeInMinutes(double speedInKmH,double distanceInKm){
        return (double) ((distanceInKm/speedInKmH) * 60);
    }
    public static float getEstimatedTimeInMinutes(float speedInKmH,float distanceInKm){
        return  ((distanceInKm/speedInKmH) * 60);
    }
    public static float getPrice(float distanceInKm, VehicleType vehicleType){
        return vehicleType.getPriceByKm() * distanceInKm;
    }
}
