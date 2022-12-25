package com.example.topgoback.Users.Service;

import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.Documents.DocumentRepository;
import com.example.topgoback.Documents.Model.Document;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.DTO.AllDriversDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.Vehicles.DTO.CreateVehicleDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Model.VehicleType;
import com.example.topgoback.Vehicles.Repository.VehicleRepository;
import com.example.topgoback.Vehicles.Repository.VehicleTypeRepository;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private GeoLocationRepository geoLocationRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private VehicleRepository vehicleRepository;


    public AllDriversDTO findAll() {

        List<Driver> drivers = driverRepository.findAll();
        List<DriverInfoDTO> ddrivers = new ArrayList<DriverInfoDTO>();
        for (Driver d:drivers
        ) {
            ddrivers.add(new DriverInfoDTO(d));
        }
        AllDriversDTO allDrivers = new AllDriversDTO();
        allDrivers.setTotalCount(drivers.size());
        allDrivers.setResults(ddrivers);

        return allDrivers;

    }

    public DriverInfoDTO addOne(CreateDriverDTO ddriver) {


        Driver driver= new Driver();
        driver.setFirstName(ddriver.getName());
        driver.setLastName(ddriver.getSurname());
        driver.setAddress(ddriver.getAddress());
        driver.setPhoneNumber(ddriver.getTelephoneNumber());
        driver.setPassword(ddriver.getPassword());
        driver.setEmail(ddriver.getEmail());

        driverRepository.save(driver);

        return new DriverInfoDTO(driver);

    }
    public DriverInfoDTO findById(Integer id)
    {
        DriverInfoDTO driverdto = new DriverInfoDTO();
        Driver driver  = driverRepository.findById(id).orElse(null);
        driverdto.setId(driver.getId());
        driverdto.setAddress(driver.getAddress());
        driverdto.setEmail(driver.getEmail());
        driverdto.setName(driver.getFirstName());
        driverdto.setProfilePicture(driver.getProfilePicture());
        driverdto.setSurname(driver.getLastName());
        driverdto.setTelephoneNumber(driver.getPhoneNumber());
        return (driverdto);
    }

    public DriverInfoDTO updateOne(DriverInfoDTO newDriver, Integer driverId) {

        Driver driver = driverRepository.findById(driverId).orElse(null);
        driver.setFirstName(newDriver.getName());
        driver.setLastName(newDriver.getSurname());
        driver.setPhoneNumber(newDriver.getTelephoneNumber());
        driver.setAddress(newDriver.getAddress());
        driver.setEmail(newDriver.getEmail());

        driverRepository.save(driver);

        return new DriverInfoDTO(driver);

    }
    public List<DocumentInfoDTO> getDriverDocuments(Integer driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        List<DocumentInfoDTO> allDocuments = new ArrayList<DocumentInfoDTO>();
        for (Document d: driver.getDocuments()
             ) {
            allDocuments.add(new DocumentInfoDTO(d));
        }
        return  allDocuments;
    }

    public DocumentInfoDTO addDriverDocument(Integer driverId, CreateDocumentDTO newDTO) {

        Driver driver = driverRepository.findById(driverId).orElse(null);
        Document document = new Document();
        document.setDocumentImage(newDTO.getDocumentImage());
        document.setDriver(driver);
        document.setName(newDTO.getName());
        documentRepository.save(document);
        driver.getDocuments().add(document);


        return new DocumentInfoDTO(document);

    }
    public Vehicle updateVehicle(GeoLocation location,Driver driver,
            VehicleType vehicleType,Vehicle vehicle,CreateVehicleDTO newVehicle)
    {
        vehicle.setCurrentLocation(location);
        vehicle.setDriver(driver);
        vehicle.setVehicleType(vehicleType);
        vehicle.setLicencePlate(newVehicle.getLicenseNumber());
        vehicle.setForAnimals(newVehicle.petTransport);
        vehicle.setForBabies(newVehicle.babyTransport);
        vehicle.setSeatNumber(newVehicle.passengerSeats);
        vehicle.setModel(newVehicle.getModel());

        return vehicle;

    }
    public GeoLocation updateLocation(GeoLocation location,CreateVehicleDTO newVehicle)
    {
        location.setAddress(newVehicle.getCurrentLocation().getAddress());
        location.setLatitude(newVehicle.getCurrentLocation().getLatitude());
        location.setLongitude(newVehicle.getCurrentLocation().getLongitude());

        return location;

    }


    public VehicleInfoDTO addDriverVehicle(Integer driverId, CreateVehicleDTO newVehicle) {
        Driver driver = driverRepository.findById(driverId).orElse(null);

        GeoLocation location = new GeoLocation();
        updateLocation(location,newVehicle);
        geoLocationRepository.save(location);

        VehicleType vehicleType = new VehicleType();
        vehicleType.setVehicleName(newVehicle.vehicleType);
        vehicleTypeRepository.save(vehicleType);

        Vehicle vehicle = new Vehicle();
        vehicle = updateVehicle(location,driver,vehicleType,vehicle,newVehicle);
        vehicleRepository.save(vehicle);

        driver.setVehicle(vehicle);
        driverRepository.save(driver);

        return new VehicleInfoDTO(vehicle);


    }

    public VehicleInfoDTO getDriverVehicle(Integer driverId) throws Exception {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        if(driver == null){
            throw new Exception("Error: No driver found!");
        }
        return new VehicleInfoDTO(driver.getVehicle());

    }

    public VehicleInfoDTO updateDriverVehicle(Integer driverId, CreateVehicleDTO newVehicle) {
        Driver driver = driverRepository.findById(driverId).orElse(null);

        GeoLocation location = new GeoLocation();
        updateLocation(location,newVehicle);
        geoLocationRepository.save(location);

        VehicleType vehicleType = new VehicleType();
        vehicleType.setVehicleName(newVehicle.vehicleType);
        vehicleTypeRepository.save(vehicleType);

        driver.setVehicle(updateVehicle(location,driver,vehicleType,driver.getVehicle(),newVehicle));
        vehicleRepository.save(driver.getVehicle());

        driverRepository.save(driver);

        return new VehicleInfoDTO(driver.getVehicle());


    }

    public WorkHoursDTO addDriverWorkingHour(Integer workingHourId, WorkHoursDTO newWorkHour) {
        return null;
    }

    public WorkHoursDTO getDriverWorkHour(Integer workingHourId) {
        return null;
    }

    public UserRidesListDTO getDriverRides(Integer driverId) {
        return null;
    }

    public DriverWorkHoursDTO getAllWorkHours(Integer driverId) {
        return null;
    }
}
