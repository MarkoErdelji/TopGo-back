package com.example.topgoback.Vehicles.Model;

import com.example.topgoback.Enums.VehicleName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String vehicleName;
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
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public float getPriceByKm() {
        return priceByKm;
    }

    public void setPriceByKm(float priceByKm) {
        this.priceByKm = priceByKm;
    }
}
