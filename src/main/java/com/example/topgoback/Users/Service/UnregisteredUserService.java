package com.example.topgoback.Users.Service;

import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Users.DTO.UnregisteredUserAssumptionDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserDTO;
import com.example.topgoback.Vehicles.Model.VehicleType;
import org.springframework.stereotype.Service;

@Service
public class UnregisteredUserService {


    public UnregisteredUserAssumptionDTO getAssumptionFromData(UnregisteredUserDTO data){

        double distance = DistanceCalculator.getDistanceFromLocations(data.getLocations().get(0).getDeparture(),data.getLocations().get(0).getDestination());

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

        assumptionDTO.setEstimatedTimeInMinutes((float) DistanceCalculator.getEstimatedTimeInMinutes(baseVehicleSpeed,distance));

        return assumptionDTO;
    }

}
