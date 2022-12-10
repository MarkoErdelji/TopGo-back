package com.example.topgoback.Users.Service;

import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.Documents.Model.Document;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.DTO.AllDriversDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Vehicles.DTO.CreateVehicleDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Model.VehicleType;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriverMokupService {

    public DriverInfoDTO addOne(CreateDriverDTO ddriver) {

        DriverInfoDTO response = getDriverInfoDTO();
        return response;
    }

    public AllDriversDTO findAll() {
        AllDriversDTO response1 = new AllDriversDTO();
        response1.setTotalCount(243);
        DriverInfoDTO response = getDriverInfoDTO();
        List<DriverInfoDTO> drivers = new ArrayList<>();
        drivers.add(response);
        response1.setResults(drivers);

        return  response1;
    }

    private static DriverInfoDTO getDriverInfoDTO() {
        DriverInfoDTO response = new DriverInfoDTO();
        response.setAddress("Bulevar Oslobodjenja 74");
        response.setEmail("pera.peric@email.com");
        response.setId(123);
        response.setName("Pera");
        response.setSurname("Perić");
        response.setProfilePicture("U3dhZ2dlciByb2Nrcw==");
        response.setTelephoneNumber("+381123123");
        return response;
    }

    public DriverInfoDTO findById(Integer driverId) {
        DriverInfoDTO response = getDriverInfoDTO();
        return response;

    }

    public DriverInfoDTO updateOne(DriverInfoDTO newDriver, Integer driverId) {
        DriverInfoDTO response = getDriverInfoDTO();
        return response;
    }

    public List<DocumentInfoDTO> getDriverDocuments(Integer driverId) {
        List<DocumentInfoDTO> response = new ArrayList<>();
        DocumentInfoDTO documentInfoDTO = new DocumentInfoDTO();
        documentInfoDTO.setId(123);
        documentInfoDTO.setDocumentImage("U3dhZ2dlciByb2Nrcw=");
        documentInfoDTO.setDriverId(10);
        documentInfoDTO.setName("Vozačka dozvola");
        response.add(documentInfoDTO);
        return response;
    }

    public DocumentInfoDTO addDriverDocument(Integer driverId, CreateDocumentDTO newDTO) {
        DocumentInfoDTO documentInfoDTO = new DocumentInfoDTO();
        documentInfoDTO.setId(123);
        documentInfoDTO.setDocumentImage("U3dhZ2dlciByb2Nrcw=");
        documentInfoDTO.setDriverId(10);
        documentInfoDTO.setName("Vozačka dozvola");
        return documentInfoDTO;
    }
    public GeoLocation updateLocation(GeoLocation location,CreateVehicleDTO newVehicle)
    {
        location.setAddress(newVehicle.getCurrentLocation().getAddress());
        location.setLatitude(newVehicle.getCurrentLocation().getLatitude());
        location.setLongitude(newVehicle.getCurrentLocation().getLongitude());

        return location;

    }

    public VehicleInfoDTO addDriverVehicle(Integer driverId, CreateVehicleDTO newVehicle) {

        return getVehicleInfoDTO();

    }

    public VehicleInfoDTO getDriverVehicle(Integer driverId) {
        return getVehicleInfoDTO();
    }

    public VehicleInfoDTO updateDriverVehicle(Integer driverId, CreateVehicleDTO newVehicle) {
        return getVehicleInfoDTO();

    }

    private static VehicleInfoDTO getVehicleInfoDTO() {
        VehicleInfoDTO response = new VehicleInfoDTO();
        GeoLocationDTO geoLocationDTO = new GeoLocationDTO();
        geoLocationDTO.setAddress("Bulevar oslobodjenja 46");
        geoLocationDTO.setLatitude(45.267136f);
        geoLocationDTO.setLongitude(19.833549f);
        response.setCurrentLocation(geoLocationDTO);
        response.setDriverId(123);
        response.setId(123);
        response.setVehicleType("STANDARDNO");
        response.setModel("VW Golf 2");
        response.setLicenseNumber("NS 123-AB");
        response.setPassengerSeats(4);
        response.setBabyTransport(true);
        response.setPetTransport(true);
        return response;
    }

    public DriverWorkHoursDTO getAllWorkHours(Integer driverId) {

        DriverWorkHoursDTO driverWorkHoursDTO = new DriverWorkHoursDTO();
        driverWorkHoursDTO.setTotalCount(243);
        List<WorkHoursDTO> hours = new ArrayList<>();
        WorkHoursDTO whd = new WorkHoursDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        whd.setEnd(LocalDateTime.parse("2022-12-10T22:39:14Z",formatter));
        whd.setStart(LocalDateTime.parse("2022-12-10T22:39:14Z",formatter));
        whd.setId(10);
        hours.add(whd);
        driverWorkHoursDTO.setResults(hours);
        return driverWorkHoursDTO;
    }

    public WorkHoursDTO addDriverWorkingHour(Integer driverId, WorkHoursDTO newWorkHour) {
        WorkHoursDTO response = new WorkHoursDTO();
        response.setId(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        response.setEnd(LocalDateTime.parse("2022-12-10T22:39:14Z",formatter));
        response.setStart(LocalDateTime.parse("2022-12-10T22:39:14Z",formatter));
        return response;
    }

    public UserRidesListDTO getDriverRides(Integer driverId) {
        UserRidesListDTO response = new UserRidesListDTO();
        response.setTotalCount(243);
        UserRideDTO ride = UserRideDTO.getMockupData();
        ArrayList<UserRideDTO> list = new ArrayList<>();
        list.add(ride);
        response.setResults(list);
        return response;

    }


    public WorkHoursDTO getDriverWorkHour(Integer driverId) {
        WorkHoursDTO response = new WorkHoursDTO();
        response.setId(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        response.setEnd(LocalDateTime.parse("2022-12-10T22:39:14Z",formatter));
        response.setStart(LocalDateTime.parse("2022-12-10T22:39:14Z",formatter));
        return response;

    }
}
