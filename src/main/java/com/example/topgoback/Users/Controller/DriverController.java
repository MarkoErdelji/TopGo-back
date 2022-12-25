package com.example.topgoback.Users.Controller;

import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.DTO.AllDriversDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Service.DriverMokupService;
import com.example.topgoback.Users.Service.DriverService;
import com.example.topgoback.Vehicles.DTO.CreateVehicleDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import com.example.topgoback.WorkHours.Service.WorkHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/driver")
@CrossOrigin(origins = "http://localhost:4200")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    WorkHoursService workHoursService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DriverInfoDTO> saveDriver(@RequestBody CreateDriverDTO ddriver) {

        DriverInfoDTO response = driverService.addOne(ddriver);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<AllDriversDTO> getAllDrivers()
    {
        AllDriversDTO response = driverService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(value = "/active")
    public ResponseEntity<AllDriversDTO> getActiveDrivers(){
        AllDriversDTO activeDrivers = driverService.getActiveDrivers();
        return new ResponseEntity<>(activeDrivers, HttpStatus.OK);
    }


    @GetMapping(value = "/{driverId}")
    public ResponseEntity<DriverInfoDTO> getDriver(@PathVariable Integer driverId)
    {

        DriverInfoDTO response = driverService.findById(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(value = "/{email}/byEmail")
    public ResponseEntity<DriverInfoDTO> getDriverByEmail(@PathVariable String email)
    {

        Driver driver = driverService.getByEmail(email);
        return new ResponseEntity<>(new DriverInfoDTO(driver), HttpStatus.OK);

    }

    @PutMapping(value = "/{driverId}",consumes = "application/json")
    public ResponseEntity<DriverInfoDTO> updateDriver(@RequestBody DriverInfoDTO newDriver,@PathVariable Integer driverId)
    {
        DriverInfoDTO response = driverService.updateOne(newDriver,driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping(value = "/{driverId}/documents")
    public ResponseEntity<List<DocumentInfoDTO>> getDriverDocuments(@PathVariable Integer driverId)
        {
            List<DocumentInfoDTO> response = driverService.getDriverDocuments(driverId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }

    @PostMapping(consumes = "application/json",value = "/{driverId}/documents")
    public ResponseEntity<DocumentInfoDTO> addDriverDocument(@PathVariable Integer driverId, @RequestBody CreateDocumentDTO newDTO)
    {
        DocumentInfoDTO response = driverService.addDriverDocument(driverId,newDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @DeleteMapping(value = "/document/{documentId}")
    public ResponseEntity<Void> addDriverDocument(@PathVariable Integer documentId)
    {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping(consumes = "application/json",value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> addDriverVehicle(@PathVariable Integer driverId, @RequestBody CreateVehicleDTO newVehicle)
    {
        VehicleInfoDTO response = driverService.addDriverVehicle(driverId,newVehicle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> getDriverVehicle(@PathVariable Integer driverId)
    {
        VehicleInfoDTO response = driverService.getDriverVehicle(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping(consumes = "application/json",value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> updateDriverVehicle(@PathVariable Integer driverId, @RequestBody CreateVehicleDTO newVehicle)
    {
        VehicleInfoDTO response = driverService.updateDriverVehicle(driverId,newVehicle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "{driverId}/working-hour")
    public ResponseEntity<DriverWorkHoursDTO> getDriverWorkingHours(@PathVariable Integer driverId)
    {
        DriverWorkHoursDTO response = driverService.getAllWorkHours(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json",value = "{driverId}/working-hour")
    public ResponseEntity<WorkHoursDTO> addDriverWorkingHour(@PathVariable Integer driverId, @RequestBody WorkHoursDTO newWorkHour)
    {
        WorkHoursDTO response = driverService.addDriverWorkingHour(driverId,newWorkHour);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{driverId}/ride")
    public ResponseEntity<UserRidesListDTO> getDriverRides(@PathVariable Integer driverId)
    {
        UserRidesListDTO response = driverService.getDriverRides(driverId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/working-hour/{workingHourId}")
    public ResponseEntity<WorkHoursDTO> getDriverWorkingHour(@PathVariable Integer workingHourId)
    {
        WorkHoursDTO response = driverService.getDriverWorkHour(workingHourId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json",value = "/working-hour/{workingHourId}")
    public ResponseEntity<WorkHoursDTO> putDriverWorkingHour(@PathVariable Integer workingHourId, @RequestBody WorkHoursDTO newWorkHour)
    {
        WorkHoursDTO response = driverService.addDriverWorkingHour(workingHourId,newWorkHour);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
