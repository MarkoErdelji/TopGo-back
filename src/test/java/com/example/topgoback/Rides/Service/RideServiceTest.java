package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.FavouriteRides.Repository.FavouriteRideRepository;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Panic.Repository.PanicRepository;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.RejectionLetters.Repository.RejectionLetterRepository;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Routes.Repository.RouteRepository;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Tools.JwtTokenUtil;
import com.example.topgoback.Users.DTO.RidePassengerDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Service.PassengerService;
import com.example.topgoback.Users.Service.UserService;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Model.VehicleType;
import com.example.topgoback.Vehicles.Repository.VehicleRepository;
import com.example.topgoback.Vehicles.Repository.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @Mock
    private RideRepository rideRepository;
    @Mock
    private VehicleTypeRepository vehicleTypeRepository;

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    PassengerRepository passengerRepository;
    @Mock
    RouteRepository routeRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    GeoLocationRepository geoLocationRepository;
    @Mock
    DriverRepository driverRepository;

    @Mock
    private UserService userService;

    @Mock
    private PassengerService passengerService;
    @Mock
    private RejectionLetterRepository rejectionLetterRepository;
    @Mock
    FavouriteRideRepository favouriteRideRepository;
    @Mock
    PanicRepository panicRepository;
    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    @InjectMocks
    private RideService rideService;


    public User mockUser(){
        User user = new User();
        user.setId(1);
        user.setEmail("ognjen34@gmail.com");
        user.setPassword("test");
        user.setFirstName("Ognjen");
        user.setLastName("Radovanovic");
        user.setBlocked(false);
        user.setAddress("Kragujevac");
        user.setPhoneNumber("0623135123");
        user.setUserNotes(new ArrayList<>());
        user.setUserType(UserType.USER);
        user.setProfilePicture(" ");
        return user;
    }

    public Passenger mockCorrectPassenger(){
        Passenger passenger = new Passenger();
        passenger.setId(1);
        passenger.setEmail("ognjen34@gmail.com");
        passenger.setPassword("test");
        passenger.setFirstName("Ognjen");
        passenger.setLastName("Radovanovic");
        passenger.setBlocked(false);
        passenger.setAddress("Kragujevac");
        passenger.setPhoneNumber("0623135123");
        passenger.setUserNotes(new ArrayList<>());
        passenger.setUserType(UserType.USER);
        passenger.setProfilePicture(" ");
        passenger.setRides(new ArrayList<>());
        passenger.setFavouriteRoutes(new ArrayList<>());
        passenger.setPayments(new ArrayList<>());
        passenger.setActive(true);
        return passenger;
    }

    public Vehicle mockCorrectVehicle(){
        VehicleType vehicleType = new VehicleType();
        vehicleType.setVehicleName(VehicleName.STANDARD.name());
        vehicleType.setId(1);
        vehicleType.setPriceByKm(100);
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(null);
        vehicle.setVehicleType(vehicleType);
        vehicle.setId(1);
        vehicle.setForBabies(true);
        vehicle.setForAnimals(true);
        vehicle.setLicencePlate("123");
        vehicle.setModel("Lamburdzini");
        vehicle.setSeatNumber(4);
        return vehicle;
    }


    public Driver mockCorrectDriver(){
        Driver driver = new Driver();
        driver.setId(100);
        driver.setEmail("ognjen34@gmail.com");
        driver.setPassword("test");
        driver.setFirstName("Ognjen");
        driver.setLastName("Radovanovic");
        driver.setBlocked(false);
        driver.setAddress("Kragujevac");
        driver.setPhoneNumber("0623135123");
        driver.setUserNotes(new ArrayList<>());
        driver.setUserType(UserType.USER);
        driver.setProfilePicture(" ");
        driver.setVehicle(mockCorrectVehicle());
        driver.setRides(new ArrayList<>());
        driver.setDocuments(new ArrayList<>());
        driver.setActive(true);
        return driver;
    }
    public Ride mockCorrectRide_Finished(){
        Ride ride = new Ride();
        ride.setId(1);
        ride.setStatus(Status.FINISHED);
        ride.setStart( LocalDateTime.of(2023, 1, 1, 0, 0));
        ride.setEnd( LocalDateTime.of(2023, 1, 1, 0, 10));
        ride.setPrice(340);
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(mockCorrectPassenger());
        ride.setPassenger(passengers);
        ride.setForAnimals(true);
        ride.setForBabies(true);
        ride.setDriver(mockCorrectDriver());
        ride.setRejectionLetter(null);
        ride.setVehicleName(VehicleName.valueOf(ride.getDriver().getVehicle().getVehicleType().getVehicleName()));
        ride.setPanic(false);
        ride.setPayment(null);
        ride.setReviews(new ArrayList<>());
        ride.setRoute(mockCorrectRoute());
        return ride;
    }
    public CreateRideDTO mockCreateRideDTO(){
        Ride ride = mockCorrectRide_Finished();
        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO route = new RouteForCreateRideDTO(ride.getRoute());
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(route);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(mockCorrectPassenger().getId());
        passengerDTO.setEmail(mockCorrectPassenger().getEmail());
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(ride.isForBabies());
        createRideDTO.setPetTransport(ride.isForAnimals());
        createRideDTO.setVehicleType(ride.getVehicleName());
        createRideDTO.setScheduledTime(null);

        return createRideDTO;
    }

    public Route mockCorrectRoute(){
        Route route = new Route();
        route.setStart(mockCorrectGeoLocation1());
        route.setFinish(mockCorrectGeoLocation2());
        route.setLenght(2);
        return route;

    }

    public GeoLocation mockCorrectGeoLocation1(){
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);
        return geoLocation;
    }

    public GeoLocation mockCorrectGeoLocation2(){
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Lovcenska 12,Novi Sad");
        geoLocation.setLongitude(19.847550F);
        geoLocation.setLatitude(45.248321F);
        return geoLocation;
    }


    @Test
    public void testfindRidesByUserId_correctData_PassengerId() {
        int userId = 1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(userService.findOne(userId)).thenReturn(mockCorrectPassenger());

        List<Ride> rides = new ArrayList<>();
        rides.add(mockCorrectRide_Finished());
        Ride ride_2 = mockCorrectRide_Finished();
        ride_2.setId(2);
        ride_2.setStart(LocalDateTime.of(2023, 1, 2, 0, 0));
        ride_2.setEnd(LocalDateTime.of(2023, 1, 2, 0, 10));
        rides.add(ride_2);

        Page<Ride> ridesPage = new PageImpl<>(rides,pageable,rides.size());

        Mockito.when(rideRepository.findByDriverOrPassengerAndStartBetween(userId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByUserId(userId,pageable,startDate,endDate);


        assertEquals(2,userRidesListDTO.getTotalCount());
        assertEquals(1,userRidesListDTO.getResults().get(0).getId());
        assertEquals(mockCorrectPassenger().getId(),userRidesListDTO.getResults().get(0).getPassengers().get(0).getId());
        assertEquals(mockCorrectPassenger().getEmail(),userRidesListDTO.getResults().get(0).getPassengers().get(0).getEmail());



    }


    @Test
    public void testfindRidesByUserId_correctData_DriverId() {
        int userId = 100;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(userService.findOne(userId)).thenReturn(mockCorrectDriver());

        List<Ride> rides = new ArrayList<>();
        rides.add(mockCorrectRide_Finished());
        Ride ride_2 = mockCorrectRide_Finished();
        ride_2.setId(2);
        ride_2.setStart(LocalDateTime.of(2023, 1, 2, 0, 0));
        ride_2.setEnd(LocalDateTime.of(2023, 1, 2, 0, 10));
        rides.add(ride_2);

        Page<Ride> ridesPage = new PageImpl<>(rides,pageable,rides.size());

        Mockito.when(rideRepository.findByDriverOrPassengerAndStartBetween(userId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByUserId(userId,pageable,startDate,endDate);


        assertEquals(2,userRidesListDTO.getTotalCount());
        assertEquals(1,userRidesListDTO.getResults().get(0).getId());
        assertEquals(mockCorrectPassenger().getId(),userRidesListDTO.getResults().get(0).getPassengers().get(0).getId());
        assertEquals(mockCorrectPassenger().getEmail(),userRidesListDTO.getResults().get(0).getPassengers().get(0).getEmail());



    }


    @Test
    public void testfindRidesByUserId_incorrectId() {
        int userId = -1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(userService.findOne(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!"));

        assertThrows(ResponseStatusException.class,()->rideService.findRidesByUserId(userId,pageable,startDate,endDate));
    }


    @Test
    public void testfindRidesByUserId_incorrectDate() {
        int userId = 1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(userService.findOne(userId)).thenReturn(mockCorrectPassenger());


        Page<Ride> ridesPage = new PageImpl<>(new ArrayList<>(),pageable,0);

        Mockito.when(rideRepository.findByDriverOrPassengerAndStartBetween(userId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByUserId(userId,pageable,startDate,endDate);


        assertEquals(0,userRidesListDTO.getTotalCount());
        assertTrue(userRidesListDTO.getResults().isEmpty());




    }
    @Test
    public void findRideByPassengerAndIsPendingPassengerHasPending()
    {
        int userId = 1;
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.PENDING);
        List<Ride> rideList = new ArrayList<>();
        rideList.add(ride);
        Mockito.when(rideRepository.findRidesByPassengeridAndIsPending(userId)).thenReturn(rideList);
        RideDTO rideDTO = rideService.findRideByPassengerAndIsPending(userId);
        assertEquals(rideDTO.status,Status.PENDING);

    }
    @Test
    public void findRideByPassengerAndIsPendingPassengerDontHavePending()
    {
        int userId = 1;
        List<Ride> rideList = new ArrayList<>();
        Mockito.when(rideRepository.findRidesByPassengeridAndIsPending(userId)).thenReturn(rideList);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.findRideByPassengerAndIsPending(userId));
        assertEquals("Pending ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }

    @Test
    public void getDriverFinishedRidesHasFinishedRides()
    {
        int userId = 100;
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.FINISHED);
        List<Ride> rideList = new ArrayList<>();
        rideList.add(ride);
        Mockito.when(rideRepository.findRidesByDriveridAndIsFinished(userId)).thenReturn(rideList);
        List<RideDTO> response = rideService.getDriverFinishedRides(userId);
        assertFalse(response.isEmpty());
        assertEquals(response.get(0).status,Status.FINISHED);
    }

    @Test
    public void getDriverFinishedRidesHasNoFinishedRides()
    {
        int userId = 100;
        List<Ride> rideList = new ArrayList<>();
        Mockito.when(rideRepository.findRidesByDriveridAndIsFinished(userId)).thenReturn(rideList);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getDriverFinishedRides(userId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void withdrawRideRideExist()
    {
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.ACCEPTED);
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        RideDTO response = rideService.withdrawRide(ride.getId());
        assertEquals(response.status,Status.CANCELED);

    }

    @Test
    public void withdrawRideRideNoExist()
    {
        int id = 150;
        Optional<Ride> optionalRide = Optional.empty();
        Mockito.when(rideRepository.findById(id)).thenReturn(optionalRide);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.withdrawRide(id));
        assertEquals("Ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void withdrawRideRideExistButNotPendingOrAccepted()
    {
        Ride ride = mockCorrectRide_Finished();
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.startRide(ride.getId()));
        assertEquals("Cannot start a ride that is not in status ACCEPTED!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());

    }
    @Test
    public void startRideRideExist()
    {
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.ACCEPTED);
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        RideDTO response = rideService.startRide(ride.getId());
        assertEquals(response.status,Status.ACTIVE);

    }

    @Test
    public void startRideRideNoExist()
    {
        int id = 150;
        Optional<Ride> optionalRide = Optional.empty();
        Mockito.when(rideRepository.findById(id)).thenReturn(optionalRide);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.startRide(id));
        assertEquals("Ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void startRideRideExistButNotAccepter()
    {
        Ride ride = mockCorrectRide_Finished();
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.startRide(ride.getId()));
        assertEquals("Cannot start a ride that is not in status ACCEPTED!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());

    }
    @Test
    public void deleteFavouriteRidesRideExist()
    {
        Ride ride = mockCorrectRide_Finished();
        FavouriteRide favouriteRide = new FavouriteRide();
        favouriteRide.setId(1);
        favouriteRide.setRoute(ride.getRoute());
        favouriteRide.setVehicleType("STANDARD");
        favouriteRide.setPassengers(ride.getPassenger());
        favouriteRide.setBabyTransport(ride.isForBabies());
        favouriteRide.setPetTransport(ride.isForAnimals());
        Optional<FavouriteRide> optionalRide = Optional.ofNullable(favouriteRide);
        Mockito.when(favouriteRideRepository.findById(favouriteRide.getId())).thenReturn(optionalRide);
        rideService.deleteFavouriteRides(favouriteRide.getId());

    }
    @Test
    public void deleteFavouriteRidesRideNoExist()
    {
        int id = 150;
        Optional<FavouriteRide> optionalRide = Optional.empty();
        Mockito.when(favouriteRideRepository.findById(id)).thenReturn(optionalRide);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.deleteFavouriteRides(id));
        assertEquals("Favorite location does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }

    @Test
    public void declineRideRideExistAndHasNewDriver()
    {
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.PENDING);
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        RejectionTextDTO rejection = new RejectionTextDTO();
        rejection.setReason("reason");
        Mockito.when(vehicleTypeRepository.findByVehicleName(ride.getVehicleName())).thenReturn(mockCorrectVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        driver.setId(101);
        driver.getVehicle().setCurrentLocation(mockCorrectGeoLocation1());
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);

        RideDTO response = rideService.declineRide(ride.getId(),rejection);
        assertEquals(response.status,Status.PENDING);
        assertEquals(response.driver.getId(),101);

    }
    @Test
    public void declineRideRideExistNoRejectionLetter()
    {
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.PENDING);
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        assertThrows(NullPointerException.class,()->rideService.declineRide(ride.getId(),null));

    }
    @Test
    public void declineRideRideExistAndNoNewDriver()
    {
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.PENDING);
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        RejectionTextDTO rejection = new RejectionTextDTO();
        rejection.setReason("reason");
        Mockito.when(vehicleTypeRepository.findByVehicleName(ride.getVehicleName())).thenReturn(mockCorrectVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(passenger.getId())).thenReturn(Optional.of(passenger));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.declineRide(ride.getId(),rejection));;
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());



    }

    @Test
    public void declineRideRideNoExist()
    {
        int id = 150;
        Optional<Ride> optionalRide = Optional.empty();
        Mockito.when(rideRepository.findById(id)).thenReturn(optionalRide);
        RejectionTextDTO rejection = new RejectionTextDTO();
        rejection.setReason("reason");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.declineRide(id,rejection));;
        assertEquals("Ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void declineRideRideExistButNotAccepted()
    {
        Ride ride = mockCorrectRide_Finished();
        Optional<Ride> optionalRide = Optional.ofNullable(ride);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(optionalRide);
        RejectionTextDTO rejection = new RejectionTextDTO();
        rejection.setReason("reason");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.declineRide(ride.getId(),rejection));;
        assertEquals("Cannot decline a ride that is not in status PENDING!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());

    }

    @Test
    public void createRideScheduledMoreThanFiveHours()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        createRideDTO.setScheduledTime(LocalDateTime.now().plusHours(10));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));;
        assertEquals("Can not order a ride thats more than 5 hours from now!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());


    }
    @Test
    public void createRideScheduledExistAtTheTime()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        createRideDTO.setScheduledTime(LocalDateTime.now().plusMinutes(25));
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.SCHEDULED);
        ride.setStart(LocalDateTime.now().plusMinutes(20));
        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(rides);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));;
        assertEquals("Scheduled ride exists at this time frame !!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());

    }
    @Test
    public void createRidePassengerDoesNotExist()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));;
        assertEquals("Passenger does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideWhileHavingPending()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.PENDING);
        passenger.getRides().add(ride);
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("Cannot create a ride while you have one already pending!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());

    }
    @Test
    public void createRideNoAvalibleDrivers()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAvalibleDriversButRejected()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.REJECTED);
        ride.setStart(LocalDateTime.now().minusMinutes(5));
        ride.getPassenger().add(passenger);
        List<Ride> rides = new ArrayList<>();
        rides.add(ride);
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(rides);
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAvalibleDriverButNotActive()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        driver.setActive(false);
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAvalibleDriverButNoPet()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        List<Driver> drivers = new ArrayList<>();
        driver.getVehicle().setForAnimals(false);
        drivers.add(driver);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAvalibleDriverWrongTypeVehicle()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        createRideDTO.setVehicleType(VehicleName.LUXURY);
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        List<Driver> drivers = new ArrayList<>();
        driver.getVehicle();
        drivers.add(driver);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAvalibleDriverHasActiveRide()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Ride ride = mockCorrectRide_Finished();
        List<Ride> rides = new ArrayList<>();
        ride.setStatus(Status.ACTIVE);
        rides.add(ride);
        Mockito.when(rideRepository.findRidesByDriveridAndIsActive(driver.getId())).thenReturn(rides);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAvalibleDriverHasAcceptedRide()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Ride ride = mockCorrectRide_Finished();
        List<Ride> rides = new ArrayList<>();
        ride.setStatus(Status.ACCEPTED);
        rides.add(ride);
        Mockito.when(rideRepository.findRidesByDriveridAndIsAccepted(driver.getId())).thenReturn(rides);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.createRide(createRideDTO));
        assertEquals("No avaliable drivers at this moment", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }
    @Test
    public void createRideAllGood()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        driver.getVehicle().setCurrentLocation(mockCorrectGeoLocation1());
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        RideDTO rideDTO = rideService.createRide(createRideDTO);
        assertEquals(rideDTO.driver.getId(), driver.getId());

    }
    @Test
    public void createRideAllGoodScheduled()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        createRideDTO.setScheduledTime(LocalDateTime.now().plusMinutes(30));
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        driver.getVehicle().setCurrentLocation(mockCorrectGeoLocation1());
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        RideDTO rideDTO = rideService.createRide(createRideDTO);
        assertEquals(Status.SCHEDULED, rideDTO.getStatus());

    }
    @Test
    public void createRideAllDriverOneCloser()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        driver.getVehicle().setCurrentLocation(mockCorrectGeoLocation1());
        Driver driver2 = mockCorrectDriver();
        driver2.setId(101);
        driver2.getVehicle().setCurrentLocation(mockCorrectGeoLocation2());
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver2);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        RideDTO rideDTO = rideService.createRide(createRideDTO);
        assertEquals(rideDTO.driver.getId(), driver.getId());

    }
    @Test
    public void createRideAllDriverTwoCloser()
    {
        CreateRideDTO createRideDTO = mockCreateRideDTO();
        Mockito.when(vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType())).thenReturn(mockCorrectDriver().getVehicle().getVehicleType());
        Passenger passenger = mockCorrectPassenger();
        Mockito.when(passengerRepository.findById(createRideDTO.getPassengers().get(0).getId())).thenReturn(Optional.of(passenger));
        Driver driver = mockCorrectDriver();
        driver.getVehicle().setCurrentLocation(mockCorrectGeoLocation2());
        Driver driver2 = mockCorrectDriver();
        driver2.setId(101);
        driver2.getVehicle().setCurrentLocation(mockCorrectGeoLocation1());
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driver);
        drivers.add(driver2);
        Mockito.when(rideRepository.findRidesByStatus(Status.REJECTED)).thenReturn(new ArrayList<>());
        Mockito.when(rideRepository.findRidesByStatus(Status.SCHEDULED)).thenReturn(new ArrayList<>());
        Mockito.when(driverRepository.findAll()).thenReturn(drivers);
        RideDTO rideDTO = rideService.createRide(createRideDTO);
        assertEquals(rideDTO.driver.getId(), driver2.getId());

    }










}
