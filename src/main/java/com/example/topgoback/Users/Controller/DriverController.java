package com.example.topgoback.Users.Controller;

import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.Enums.AllowedSortFields;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Service.RideService;
import com.example.topgoback.Users.DTO.AllActiveDriversDTO;
import com.example.topgoback.Users.DTO.AllDriversDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Service.DriverService;
import com.example.topgoback.Vehicles.DTO.CreateVehicleDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.EndTimeDTO;
import com.example.topgoback.WorkHours.DTO.StartTimeDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import com.example.topgoback.WorkHours.Model.WorkHours;
import com.example.topgoback.WorkHours.Service.WorkHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "api/driver")
@CrossOrigin(origins = "http://localhost:4200")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @Autowired
    WorkHoursService workHoursService;
    @Autowired
    RideService rideService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<DriverInfoDTO> saveDriver(@RequestBody CreateDriverDTO ddriver) {

        DriverInfoDTO response = driverService.addOne(ddriver);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<?> getAllDrivers(@RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "10") Integer size,
                                           Pageable pageable)
    {
        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        AllDriversDTO response = driverService.findAll(pageable);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/active")
    public ResponseEntity<AllActiveDriversDTO> getActiveDrivers(){
        AllActiveDriversDTO activeDrivers = driverService.getActiveDrivers();
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
    public ResponseEntity<?> getDriverDocuments(@PathVariable Integer driverId)
        {
            List<DocumentInfoDTO> response = driverService.getDriverDocuments(driverId);
            if(response == null){
                return new ResponseEntity<>("Driver doesn't exist",HttpStatus.BAD_REQUEST);
            }
            else {
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

    @PostMapping(consumes = "application/json",value = "/{driverId}/documents")
    public ResponseEntity<DocumentInfoDTO> addDriverDocument(@PathVariable Integer driverId, @RequestBody CreateDocumentDTO newDTO)
    {
        DocumentInfoDTO response = driverService.addDriverDocument(driverId,newDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @DeleteMapping(value = "/document/{documentId}")
    public ResponseEntity<?> deleteDriverDocument(@PathVariable Integer documentId)
    {
        driverService.deleteDriverDocument(documentId);
        return new ResponseEntity<>("Driver document deleted successfully", HttpStatus.NO_CONTENT);

    }

    @PostMapping(consumes = "application/json",value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> addDriverVehicle(@PathVariable Integer driverId, @RequestBody CreateVehicleDTO newVehicle)
    {
        VehicleInfoDTO response = driverService.addDriverVehicle(driverId,newVehicle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{driverId}/vehicle")
    public ResponseEntity<?> getDriverVehicle(@PathVariable Integer driverId)
    {
        try {
            VehicleInfoDTO response = driverService.getDriverVehicle(driverId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(consumes = "application/json",value = "/{driverId}/vehicle")
    public ResponseEntity<VehicleInfoDTO> updateDriverVehicle(@PathVariable Integer driverId, @RequestBody CreateVehicleDTO newVehicle)
    {
        VehicleInfoDTO response = driverService.updateDriverVehicle(driverId,newVehicle);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json",value = "{driverId}/working-hour")
    public ResponseEntity<WorkHoursDTO> addDriverWorkingHour(@PathVariable Integer driverId, @RequestBody StartTimeDTO start)
    {
        WorkHours workHours = workHoursService.addOne(start.getStart(), driverId);
        return new ResponseEntity<>(new WorkHoursDTO(workHours), HttpStatus.OK);
    }

    @GetMapping(value = "{id}/working-hour")
    public ResponseEntity<?> getDriverWorkingHours(@PathVariable Integer id,
                                                   @RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginDateInterval,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateInterval,
                                                   Pageable pageable)
    {
        if(beginDateInterval == null){
            beginDateInterval = LocalDateTime.of(0001, 01, 01, 00, 00, 00, 00);;
        }
        if(endDateInterval == null){
            endDateInterval = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999);
        }
        pageable = (Pageable) PageRequest.of(page, size);
        DriverWorkHoursDTO driverWorkHoursDTO = workHoursService.findWorkingHoursByDriversId(id, pageable, beginDateInterval, endDateInterval);
        if(driverWorkHoursDTO == null){
            return new ResponseEntity<>("Driver has no WorkingHours!",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(driverWorkHoursDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/ride")
    public ResponseEntity<?> getDriverRides(@PathVariable Integer id,
                                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(required = false, defaultValue =  "10") Integer size,
                                                           @RequestParam(required = false) String sort,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime beginDateInterval,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateInterval,
                                                           Pageable pageable)
    {
        if(sort == null){
            sort = "id";
        }
        else{
            boolean isValidSortField = false;
            for (AllowedSortFields allowedField : AllowedSortFields.values()) {
                if (sort.equals(allowedField.getField())) {
                    isValidSortField = true;
                    break;
                }
            }
            if (!isValidSortField) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid sort field. Allowed fields: " + Arrays.toString(AllowedSortFields.values()));
            }
        }
        if(beginDateInterval == null){
            beginDateInterval = LocalDateTime.of(0001, 01, 01, 00, 00, 00, 00);;
        }
        if(endDateInterval == null){
            endDateInterval = LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999);
        }
        pageable = (Pageable) PageRequest.of(page, size, Sort.by(sort).ascending());

        UserRidesListDTO rides = rideService.findRidesByDriversId(id,pageable,beginDateInterval,endDateInterval);


        if(rides == null){
            return new ResponseEntity<>("User has no rides!",HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(rides, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/working-hour/{workingHourId}")
    public ResponseEntity<?> getSpecificWorkingHour(@PathVariable Integer workingHourId){
        WorkHours workHours = workHoursService.findById(workingHourId);
        return  new ResponseEntity<>(new WorkHoursDTO(workHours), HttpStatus.OK);

    }

    @PutMapping(consumes = "application/json",value = "/working-hour/{workingHourId}")
    public ResponseEntity<WorkHoursDTO> putDriverWorkingHour(@PathVariable Integer workingHourId, @RequestBody EndTimeDTO end)
    {
        WorkHours workHours = workHoursService.updateOne(workingHourId, end.getEnd());
        return new ResponseEntity<>(new WorkHoursDTO(workHours), HttpStatus.OK);
    }





}
