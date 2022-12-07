package com.example.topgoback.Users.Controller;

import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.Users.DTO.AllDriversDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Service.DriverService;
import com.example.topgoback.Vehicles.DTO.CreateVehicleDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.Service.WorkHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/driver")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    WorkHoursService workHoursService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DriverInfoDTO> saveDriver(@RequestBody CreateDriverDTO ddriver) {

        DriverInfoDTO response = driverService.addOne(ddriver);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<AllDriversDTO> getAllDrivers()
    {
        AllDriversDTO response = driverService.findAll();

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/{driverId}")
    public ResponseEntity<DriverInfoDTO> getDriver(@PathVariable Integer driverId)
    {
        Driver driver = driverService.findById(driverId);
        if (driver == null)
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        DriverInfoDTO response = new DriverInfoDTO(driver);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }

    @PutMapping(value = "/{driverId}",consumes = "application/json")
    public ResponseEntity<DriverInfoDTO> updateDriver(@RequestBody DriverInfoDTO newDriver,@PathVariable Integer driverId)
    {
        DriverInfoDTO response = driverService.updateOne(newDriver,driverId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }

    @GetMapping(value = "/{driverId}/documents")
    public ResponseEntity<List<DocumentInfoDTO>> getDriverDocuments(@PathVariable Integer driverId)
        {
            List<DocumentInfoDTO> response = driverService.getDriverDocuments(driverId);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        }
    @PostMapping(consumes = "application/json",value = "/{driverId}/documents")
    public ResponseEntity<DocumentInfoDTO> addDriverDocument(@PathVariable Integer driverId, @RequestBody CreateDocumentDTO newDTO)
    {
        DocumentInfoDTO response = driverService.addDriverDocument(driverId,newDTO);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }

    @PostMapping(consumes = "application/json",value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> addDriverVehicle(@PathVariable Integer driverId, @RequestBody CreateVehicleDTO newVehicle)
    {
        VehicleInfoDTO response = driverService.addDriverVehicle(driverId,newVehicle);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> getDriverVehicle(@PathVariable Integer driverId)
    {
        VehicleInfoDTO response = driverService.getDriverVehicle(driverId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping(consumes = "application/json",value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> updateDriverVehicle(@PathVariable Integer driverId, @RequestBody CreateVehicleDTO newVehicle)
    {
        VehicleInfoDTO response = driverService.updateDriverVehicle(driverId,newVehicle);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "{driverId}/working-hours")
    public ResponseEntity<DriverWorkHoursDTO> getDriverWorkingHours(@PathVariable Integer driverId)
    {
        DriverWorkHoursDTO response = workHoursService.getAllWorkHours(driverId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }






}
