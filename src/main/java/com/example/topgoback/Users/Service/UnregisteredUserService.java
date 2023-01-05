package com.example.topgoback.Users.Service;

import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserAssumptionDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserDTO;
import com.example.topgoback.Vehicles.Model.VehicleType;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredUserService {


    public UnregisteredUserAssumptionDTO getAssumptionFromData(UnregisteredUserDTO data){

        double distance = getDistanceFromLocations(data.getLocations().get(0).getDeparture(),data.getLocations().get(0).getDestinations());

        double basePrice = 100;
        double vehicleModifier = 1;
        double baseVehicleSpeed = 45;

        if(data.getVehicleType().matches(VehicleName.LUXURY.toString())){
            vehicleModifier += 0.5;
        }
        else if (data.getVehicleType().matches(VehicleName.VAN.toString())){
            vehicleModifier += 0.2;
        }

        if(data.getBabyTransport()){
            vehicleModifier += 0.1;
        }
        if(data.getPetTransport()){
            vehicleModifier += 0.1;
        }

        double fullPrice = vehicleModifier*basePrice+distance*120;

        UnregisteredUserAssumptionDTO assumptionDTO = new UnregisteredUserAssumptionDTO();

        assumptionDTO.setEstimatedCost((float) fullPrice);
        assumptionDTO.setEstimatedTimeInMinutes((float) (distance/baseVehicleSpeed)*60);

        return assumptionDTO;
    }

    private Double getDistanceFromLocations(GeoLocationDTO startLocation, GeoLocationDTO endLocation){
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
}
