package com.example.topgoback.Vehicles.Model;

import com.example.topgoback.Enums.VehicleName;
import jakarta.persistence.*;

@Entity
public class VehicleType {
    @Id
    @SequenceGenerator(name = "mySeqGenVehicleType", sequenceName = "mySeqGenVehicleType", initialValue = 4, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenVehicleType")
    private Integer id;
    private VehicleName vehicleName;
    private float priceByKm;

    public VehicleType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVehicleName() {
        return vehicleName.name();
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName= VehicleName.valueOf(vehicleName);
    }

    public float getPriceByKm() {
        return priceByKm;
    }

    public void setPriceByKm(float priceByKm) {
        this.priceByKm = priceByKm;
    }
}
