package com.example.topgoback.GeoLocations.Repository;

import com.example.topgoback.GeoLocations.Model.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoLocationRepository extends JpaRepository<GeoLocation,Integer> {
}
