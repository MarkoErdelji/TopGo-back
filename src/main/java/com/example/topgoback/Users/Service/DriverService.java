package com.example.topgoback.Users.Service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.Documents.DocumentRepository;
import com.example.topgoback.Documents.Model.Document;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.DTO.AllActiveDriversDTO;
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
import com.example.topgoback.Vehicles.Service.VehicleTypeService;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private PasswordEncoder passwordEncoder;



    public AllDriversDTO findAll(Pageable pageable) {

        Page<Driver> drivers = driverRepository.findAll(pageable);
        List<DriverInfoDTO> ddrivers = new ArrayList<DriverInfoDTO>();
        for (Driver d:drivers
        ) {
            ddrivers.add(new DriverInfoDTO(d));
        }
        AllDriversDTO allDrivers = new AllDriversDTO(new PageImpl<>(ddrivers, pageable, drivers.getTotalElements()));
        return allDrivers;

    }

    public DriverInfoDTO addOne(CreateDriverDTO ddriver) {
        Optional<Driver> existingDriver = Optional.ofNullable(driverRepository.findByEmail(ddriver.getEmail()));
        if(existingDriver.isEmpty()){
            Driver driver = new Driver();
            driver.setProfilePicture(ddriver.getProfilePicture());
            driver.setFirstName(ddriver.getName());
            driver.setLastName(ddriver.getSurname());
            driver.setAddress(ddriver.getAddress());
            driver.setPhoneNumber(ddriver.getTelephoneNumber());
            String hashedPassword = passwordEncoder.encode(ddriver.getPassword());
            driver.setPassword(hashedPassword);
            driver.setEmail(ddriver.getEmail());
            driver.setUserType(UserType.DRIVER);
            driverRepository.save(driver);
            return new DriverInfoDTO(driver);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with that email already exists!");
    }
    public DriverInfoDTO findById(Integer id)
    {

        Optional<Driver> driver  = driverRepository.findById(id);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }
        DriverInfoDTO driverdto = new DriverInfoDTO();
        driverdto.setId(driver.get().getId());
        driverdto.setAddress(driver.get().getAddress());
        driverdto.setEmail(driver.get().getEmail());
        driverdto.setName(driver.get().getFirstName());
        driverdto.setProfilePicture(driver.get().getProfilePicture());
        driverdto.setSurname(driver.get().getLastName());
        driverdto.setTelephoneNumber(driver.get().getPhoneNumber());
        return (driverdto);
    }

    public DriverInfoDTO updateOne(DriverInfoDTO newDriver, Integer driverId) {

        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }
        driver.get().setFirstName(newDriver.getName());
        driver.get().setLastName(newDriver.getSurname());
        driver.get().setPhoneNumber(newDriver.getTelephoneNumber());
        driver.get().setProfilePicture(newDriver.getProfilePicture());
        driver.get().setAddress(newDriver.getAddress());
        driver.get().setEmail(newDriver.getEmail());

        driverRepository.save(driver.get());

        return new DriverInfoDTO(driver.get());

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

        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }
        Document document = new Document();
        document.setDocumentImage(newDTO.getDocumentImage());
        document.setDriver(driver.get());
        document.setName(newDTO.getName());
        documentRepository.save(document);
        driver.get().getDocuments().add(document);


        return new DocumentInfoDTO(document);

    }
    public void deleteDriverDocument(int id){
        Optional<Document> document = documentRepository.findById(id);
        if(document.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document does not exist!");
        }
        documentRepository.deleteById(id);
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
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }
        GeoLocation location = new GeoLocation();
        updateLocation(location,newVehicle);
        geoLocationRepository.save(location);


        String vehileTypeName = newVehicle.vehicleType;
        VehicleType vehicleType = switch (vehileTypeName) {
            case "STANDARD" -> this.vehicleTypeRepository.findById(1).orElseGet(null);
            case "LUXURY" -> this.vehicleTypeRepository.findById(2).orElseGet(null);
            case "VAN" -> this.vehicleTypeRepository.findById(3).orElseGet(null);
            default -> new VehicleType();
        };

        Vehicle vehicle = new Vehicle();
        vehicle = updateVehicle(location,driver.get(),vehicleType,vehicle,newVehicle);
        vehicleRepository.save(vehicle);

        driver.get().setVehicle(vehicle);
        driverRepository.save(driver.get());

        return new VehicleInfoDTO(vehicle);


    }

    public VehicleInfoDTO getDriverVehicle(Integer driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }
        if(driver.get().getVehicle() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle is not assigned!");
        }
        return new VehicleInfoDTO(driver.get().getVehicle());

    }

    public VehicleInfoDTO updateDriverVehicle(Integer driverId, CreateVehicleDTO newVehicle) {
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }

        GeoLocation location = new GeoLocation();
        updateLocation(location,newVehicle);
        geoLocationRepository.save(location);

        VehicleType vehicleType = new VehicleType();
        vehicleType.setVehicleName(newVehicle.vehicleType);
        vehicleTypeRepository.save(vehicleType);

        driver.get().setVehicle(updateVehicle(location,driver.get(),vehicleType,driver.get().getVehicle(),newVehicle));
        vehicleRepository.save(driver.get().getVehicle());

        driverRepository.save(driver.get());

        return new VehicleInfoDTO(driver.get().getVehicle());


    }
    public AllActiveDriversDTO getActiveDrivers (){
        List<Driver> drivers = driverRepository.findByIsActiveTrue();
        List<DriverInfoDTO> driversDTO = new ArrayList<>();
        for (Driver d:drivers
        ) {
            driversDTO.add(new DriverInfoDTO(d));
        }
        AllActiveDriversDTO allActiveDrivers = new AllActiveDriversDTO();
        allActiveDrivers.setTotalCount(drivers.size());
        allActiveDrivers.setResults(driversDTO);

        return allActiveDrivers;
    }

    public Driver getByEmail(String email){
        return this.driverRepository.findByEmail(email);
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
