package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideDTO;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.FavouriteRides.Repository.FavouriteRideRepository;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Panic.DTO.PanicDTO;
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
import com.example.topgoback.Users.DTO.UserRef;

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
import org.junit.jupiter.api.Assertions;
import static org.assertj.core.api.Assertions.assertThat;
import org.h2.engine.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.*;
import org.springframework.data.geo.Distance;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.ClassBasedNavigableIterableAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
        assertEquals(mockCorrectDriver().getId(),userRidesListDTO.getResults().get(0).getDriver().getId());
        assertEquals(mockCorrectDriver().getEmail(),userRidesListDTO.getResults().get(0).getDriver().getEmail());



    }

    @Test
    public void testfindRidesByUserId_incorrectId() {
        int userId = -1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(userService.findOne(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.findRidesByUserId(userId,pageable,startDate,endDate));;
        assertEquals("User does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());    }


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
    public void testfindRidesByDriver_correctData_DriverId() {
        int userId = 100;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(driverRepository.findById(userId)).thenReturn(Optional.of(mockCorrectDriver()));

        List<Ride> rides = new ArrayList<>();
        rides.add(mockCorrectRide_Finished());
        Ride ride_2 = mockCorrectRide_Finished();
        ride_2.setId(2);
        ride_2.setStart(LocalDateTime.of(2023, 1, 2, 0, 0));
        ride_2.setEnd(LocalDateTime.of(2023, 1, 2, 0, 10));
        rides.add(ride_2);

        Page<Ride> ridesPage = new PageImpl<>(rides, pageable, rides.size());

        Mockito.when(rideRepository.findByDriverAndBeginBetween(userId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByDriversId(userId, pageable, startDate, endDate);


        assertEquals(2, userRidesListDTO.getTotalCount());
        assertEquals(1, userRidesListDTO.getResults().get(0).getId());
        assertEquals(mockCorrectPassenger().getId(), userRidesListDTO.getResults().get(0).getPassengers().get(0).getId());
        assertEquals(mockCorrectPassenger().getEmail(), userRidesListDTO.getResults().get(0).getPassengers().get(0).getEmail());
    }


    @Test
    public void testFindRidesByPassengerId_correctData() {
        int passengerId = 1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(passengerService.findById(passengerId)).thenReturn(mockCorrectPassenger());

        List<Ride> rides = new ArrayList<>();
        rides.add(mockCorrectRide_Finished());
        Ride ride_2 = mockCorrectRide_Finished();
        ride_2.setId(2);
        ride_2.setStart(LocalDateTime.of(2023, 1, 2, 0, 0));
        ride_2.setEnd(LocalDateTime.of(2023, 1, 2, 0, 10));
        rides.add(ride_2);

        Page<Ride> ridesPage = new PageImpl<>(rides,pageable,rides.size());

        Mockito.when(rideRepository.findByPassengerAndBeginBetween(passengerId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByPassengerId(passengerId,pageable,startDate,endDate);


        assertEquals(2,userRidesListDTO.getTotalCount());
        assertEquals(1,userRidesListDTO.getResults().get(0).getId());
        assertEquals(mockCorrectPassenger().getId(),userRidesListDTO.getResults().get(0).getPassengers().get(0).getId());
        assertEquals(mockCorrectPassenger().getEmail(),userRidesListDTO.getResults().get(0).getPassengers().get(0).getEmail());
    }

    @Test
    public void testFindRidesByPassengerId_incorrectData() {
        int passengerId = -1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(passengerService.findById(passengerId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger does not exist!"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.findRidesByPassengerId(passengerId,pageable,startDate,endDate));;
        assertEquals("Passenger does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());    }


    @Test
    public void testFindRidesByPassengerId_incorrectDate() {
        int passengerId = 1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(passengerService.findById(passengerId)).thenReturn(mockCorrectPassenger());


        Page<Ride> ridesPage = new PageImpl<>(new ArrayList<>(),pageable,0);

        Mockito.when(rideRepository.findByPassengerAndBeginBetween(passengerId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByPassengerId(passengerId,pageable,startDate,endDate);


        assertEquals(0,userRidesListDTO.getTotalCount());
        assertTrue(userRidesListDTO.getResults().isEmpty());


    }
    @Test
    public void testFindRidesByPassengerId_incorrectData_DriverId() {
        int passengerId = 1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(passengerService.findById(passengerId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger does not exist!"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.findRidesByPassengerId(passengerId,pageable,startDate,endDate));;
        assertEquals("Passenger does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());

    }

    @Test
    public void testGetPassengersAcceptedRide_correctData_hasAccepted() {
        int passengerId = 1;

        LocalDateTime startTime = LocalDateTime.now();
        Ride acceptedRide = mockCorrectRide_Finished();
        acceptedRide.setStatus(Status.ACCEPTED);
        acceptedRide.setStart(startTime);
        acceptedRide.setEnd(null);
        List<Ride> rides = new ArrayList<>();
        rides.add(acceptedRide);
        Mockito.when(rideRepository.findRidesByPassengeridAndIsAccepted(passengerId)).thenReturn(rides);

        RideDTO acceptedRideDTO = rideService.getPassengersAcceptedRide(passengerId);
        assertEquals(Status.ACCEPTED,acceptedRideDTO.status);
        assertEquals(startTime,acceptedRideDTO.startTime);
        assertNull(acceptedRideDTO.endTime);
    }


    @Test
    public void testGetPassengersAcceptedRide_correctData_noAcceptedRides() {
        int passengerId = 1;


        Mockito.when(rideRepository.findRidesByPassengeridAndIsAccepted(passengerId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getPassengersAcceptedRide(passengerId));
        assertEquals("Accepted ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testGetPassengersAcceptedRide_incorrectData() {
        int passengerId = -1;

        Mockito.when(rideRepository.findRidesByPassengeridAndIsAccepted(passengerId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getPassengersAcceptedRide(passengerId));
        assertEquals("Accepted ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }


    @Test
    public void testGetPassengersAcceptedRide_incorrectData_driverId() {
        int passengerId = 100;

        Mockito.when(rideRepository.findRidesByPassengeridAndIsAccepted(passengerId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getPassengersAcceptedRide(passengerId));
        assertEquals("Accepted ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
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
    public void startRideRideExistButNotAccepted()
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

    @Test
    public void testfindRidesByDriverId_incorrectId() {
        int userId = -1;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(driverRepository.findById(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!"));

        assertThrows(ResponseStatusException.class,()->rideService.findRidesByDriversId(userId,pageable,startDate,endDate));
    }

    @Test
    public void testfindRidesByDriverId_incorrectDate() {
        int userId = 100;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(driverRepository.findById(userId)).thenReturn(Optional.ofNullable(mockCorrectDriver()));


        Page<Ride> ridesPage = new PageImpl<>(new ArrayList<>(),pageable,0);

        Mockito.when(rideRepository.findByDriverAndBeginBetween(userId, startDate, endDate, pageable)).thenReturn(ridesPage);

        UserRidesListDTO userRidesListDTO = rideService.findRidesByDriversId(userId,pageable,startDate,endDate);


        assertEquals(0,userRidesListDTO.getTotalCount());
        assertTrue(userRidesListDTO.getResults().isEmpty());


    }

    @Test
    public void testCancelRide_RidePending(){
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.PENDING);
        ride.setEnd(null);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("reason");
        RideDTO rideDTO = rideService.cancelRide(ride.getId(), rejectionTextDTO);
        assertEquals(ride.getId(), rideDTO.id);
        assertEquals(Status.REJECTED, rideDTO.status);

    }

    @Test
    public void testCancelRide_RideAccepted(){
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.ACCEPTED);
        ride.setEnd(null);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("reason");
        RideDTO rideDTO = rideService.cancelRide(ride.getId(), rejectionTextDTO);
        assertEquals(ride.getId(), rideDTO.id);
        assertEquals(Status.REJECTED, rideDTO.status);

    }

    @Test
    public void testCancleRide_InvalidRideStatus(){
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("reason");
        Ride ride = mockCorrectRide_Finished();
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,() -> {
            rideService.cancelRide(ride.getId(), rejectionTextDTO);
        });

        assertThat(exception.getMessage()).contains("Cannot cancel a ride that is not in status PENDING or ACCEPTED!");
        assertEquals(exception.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    public void testCancelRide_InvalidRide(){
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("reason");
        Mockito.when(rideRepository.findById(-1)).thenReturn(Optional.empty());
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,() -> {
            rideService.cancelRide(-1, rejectionTextDTO);
        });
        assertThat(exception.getMessage()).contains("Ride does not exist!");
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void testGetDriverAcceptedRide_CorrectDriverId(){
        List<Ride> rides = new ArrayList<Ride>();
        Driver driver = mockCorrectDriver();
        Ride ride1 = mockCorrectRide_Finished();
        ride1.setStatus(Status.ACCEPTED);
        ride1.setEnd(null);
        rides.add(ride1);
        Ride ride2 = mockCorrectRide_Finished();
        ride2.setId(2);
        ride2.setStatus(Status.ACCEPTED);
        ride2.setEnd(null);
        rides.add(ride2);
        Mockito.when(rideRepository.findRidesByDriveridAndIsAccepted(driver.getId())).thenReturn(rides);
        RideDTO rideDTO = rideService.getDriverAcceptedRide(driver.getId());
        assertEquals(rideDTO.getId(), rides.get(0).getId());
        assertEquals(rideDTO.getStatus(), rides.get(0).getStatus());
    }
    
    @Test
    public void testGetDriverAcceptedRide_NoAcceptedRide(){
        List<Ride> rides = new ArrayList<Ride>();
        Driver driver = mockCorrectDriver();
        Mockito.when(rideRepository.findRidesByDriveridAndIsAccepted(driver.getId())).thenReturn(rides);
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,() -> {
            rideService.getDriverAcceptedRide(driver.getId());
        });
        assertThat(exception.getMessage()).contains("Active ride does not exist!");
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void testGetRideById_CorrectId(){
        Ride ride  = mockCorrectRide_Finished();
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        RideDTO rideDTO = rideService.getRideById(ride.getId());
        assertEquals(rideDTO.getId(), ride.getId());
        assertEquals(rideDTO.getStatus(), ride.getStatus());
    }

    @Test
    public void testGetRideById_InCorrectId(){
        Mockito.when(rideRepository.findById(-1)).thenReturn(Optional.empty());
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,() -> {
            rideService.getRideById(-1);
        });
        assertThat(exception.getMessage()).contains("Ride does not exist!");
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);

    }

    @Test
    public void testEndRide_CorrectRideId(){
        Ride ride = mockCorrectRide_Finished();
        ride.setStatus(Status.ACTIVE);
        ride.setEnd(null);
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        RideDTO rideDTO = rideService.endRide(ride.getId());
        assertEquals(rideDTO.getId(), ride.getId());
        assertEquals(Status.FINISHED, rideDTO.getStatus());
    }

    @Test
    public void testEndRide_InCorrectRideId(){
        Mockito.when(rideRepository.findById(-1)).thenReturn(Optional.empty());
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,() -> {
            rideService.endRide(-1);
        });
        assertThat(exception.getMessage()).contains("Ride does not exist!");
        assertEquals(exception.getStatusCode(), HttpStatus.NOT_FOUND);

    }
    @Test
    public void testEndRide_IncorrectRideStatus(){
        Ride ride = mockCorrectRide_Finished();
        Mockito.when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,() -> {
            rideService.endRide(ride.getId());
        });
        assertThat(exception.getMessage()).contains("Cannot end a ride that is not in status FINISHED!");
        assertEquals(exception.getStatusCode(), HttpStatus.BAD_REQUEST);
    }










    @Test
    public void testGetDriverActiveRide_correctData_hasActive() {
        int driverId = 100;

        LocalDateTime startTime = LocalDateTime.now();
        Ride acceptedRide = mockCorrectRide_Finished();
        acceptedRide.setStatus(Status.ACTIVE);
        acceptedRide.setStart(startTime);
        acceptedRide.setEnd(null);
        List<Ride> rides = new ArrayList<>();
        rides.add(acceptedRide);
        Mockito.when(rideRepository.findRidesByDriveridAndIsActive(driverId)).thenReturn(rides);

        RideDTO acceptedRideDTO = rideService.getDriverActiveRide(driverId);
        assertEquals(Status.ACTIVE,acceptedRideDTO.status);
        assertEquals(startTime,acceptedRideDTO.startTime);
        assertNull(acceptedRideDTO.endTime);
    }


    @Test
    public void testGetDriverActiveRide_correctData_noActiveRides() {
        int driverId = 100;


        Mockito.when(rideRepository.findRidesByDriveridAndIsActive(driverId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getDriverActiveRide(driverId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testGetDriverActiveRide_incorrectData() {
        int driverId = -1;

        Mockito.when(rideRepository.findRidesByDriveridAndIsActive(driverId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getDriverActiveRide(driverId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }


    @Test
    public void testGetDriverActiveRide_incorrectData_passengerId() {
        int driverId = 1;

        Mockito.when(rideRepository.findRidesByDriveridAndIsActive(driverId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getDriverActiveRide(driverId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }


    @Test
    public void testGetPassengerActiveRide_correctData_hasActive() {
        int passengerId = 1;

        LocalDateTime startTime = LocalDateTime.now();
        Ride acceptedRide = mockCorrectRide_Finished();
        acceptedRide.setStatus(Status.ACTIVE);
        acceptedRide.setStart(startTime);
        acceptedRide.setEnd(null);
        List<Ride> rides = new ArrayList<>();
        rides.add(acceptedRide);
        Mockito.when(rideRepository.findRidesByPassengeridAndIsActive(passengerId)).thenReturn(rides);

        RideDTO acceptedRideDTO = rideService.getPassengerActiveRide(passengerId);
        assertEquals(Status.ACTIVE,acceptedRideDTO.status);
        assertEquals(startTime,acceptedRideDTO.startTime);
        assertNull(acceptedRideDTO.endTime);
    }


    @Test
    public void testGetPassengerActiveRide_correctData_noActiveRides() {
        int passengerId = 1;


        Mockito.when(rideRepository.findRidesByPassengeridAndIsActive(passengerId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getPassengerActiveRide(passengerId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testGetPassengerActiveRide_incorrectData() {
        int passengerId = -1;

        Mockito.when(rideRepository.findRidesByPassengeridAndIsActive(passengerId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getPassengerActiveRide(passengerId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }


    @Test
    public void testGetPassengerActiveRide_incorrectData_driverId() {
        int passengerId = 100;

        Mockito.when(rideRepository.findRidesByPassengeridAndIsActive(passengerId)).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.getPassengerActiveRide(passengerId));
        assertEquals("Active ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testPanic_correctData_passengerHeader(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(UserType.USER.name());
        Mockito.when(passengerRepository.findById(1)).thenReturn(Optional.ofNullable(mockCorrectPassenger()));

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);
        Mockito.when(rideRepository.findById(1)).thenReturn(Optional.of(current_ride));
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");
        PanicDTO dto = rideService.panic(current_ride.getId(),rejectionTextDTO,authorization);

        assertEquals(current_ride.getId(),dto.getRide().getId());
        assertEquals(current_ride.getPassenger().get(0).getId(),dto.getRide().getPassengers().get(0).getId());
        assertTrue(dto.getTime().isBefore(LocalDateTime.now()));
        assertEquals("myReason",dto.getReason());
        assertEquals(1,dto.getUser().getId());
    }



    @Test
    public void testPanic_correctData_driverHeader(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(100);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(UserType.DRIVER.name());
        Mockito.when(driverRepository.findById(100)).thenReturn(Optional.ofNullable(mockCorrectDriver()));

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);
        Mockito.when(rideRepository.findById(1)).thenReturn(Optional.of(current_ride));
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");
        PanicDTO dto = rideService.panic(current_ride.getId(),rejectionTextDTO,authorization);

        assertEquals(current_ride.getId(),dto.getRide().getId());
        assertEquals(current_ride.getDriver().getId(),dto.getRide().getDriver().getId());
        assertEquals("myReason",dto.getReason());
        assertEquals(current_ride.getDriver().getId(),dto.getUser().getId());
    }



    @Test
    public void testPanic_incorrectData_incorrectHeaderRole(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(100);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn("WrongRole");

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);

        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Header has no correct role!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }

    @Test
    public void testPanic_incorrectData_incorrectHeaderId_RoleUser(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(-1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn("USER");

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);

        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Passenger does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testPanic_incorrectData_incorrectHeaderId_RoleDriver(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(-1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn("DRIVER");

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);

        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Driver does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testPanic_incorrectData_incorrectHeaderId_IncorrectRole(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(-1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn("Incorrect_role");

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);


        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Header has no correct role!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }


    @Test
    public void testPanic_incorrectData_incorrectRideId(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(UserType.USER.name());

        LocalDateTime startTime = LocalDateTime.now();
        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setId(-1);
        current_ride.setStart(startTime);
        current_ride.setEnd(null);
        current_ride.setStatus(Status.ACTIVE);
        Mockito.when(rideRepository.findById(-1)).thenReturn(Optional.empty());

        Mockito.when(passengerRepository.findById(1)).thenReturn(Optional.ofNullable(mockCorrectPassenger()));

        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Ride does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }


    @Test
    public void testPanic_incorrectData_rideNotActive(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(UserType.USER.name());

        Ride current_ride = mockCorrectRide_Finished();
        Mockito.when(rideRepository.findById(1)).thenReturn(Optional.ofNullable(current_ride));

        Mockito.when(passengerRepository.findById(1)).thenReturn(Optional.ofNullable(mockCorrectPassenger()));

        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Cannot panic on a ride than is not ACTIVE!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }

    @Test
    public void testPanic_incorrectData_authorizationNull(){
        String authorization = null;

        Ride current_ride = mockCorrectRide_Finished();


        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Header is invalid!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }


    @Test
    public void testPanic_incorrectData_rejectionTextDTONull(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(UserType.USER.name());

        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setStatus(Status.ACTIVE);
        Mockito.when(rideRepository.findById(1)).thenReturn(Optional.ofNullable(current_ride));

        Mockito.when(passengerRepository.findById(1)).thenReturn(Optional.ofNullable(mockCorrectPassenger()));

        RejectionTextDTO rejectionTextDTO = null;

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(current_ride.getId(),rejectionTextDTO,authorization));
        assertEquals("Reason can not be null!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }


    @Test
    public void testPanic_incorrectData_rideIdNull(){
        String authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg";

        Mockito.when(jwtTokenUtil.getUserIdFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(1);
        Mockito.when(jwtTokenUtil.getRoleFromToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlcmRlbGppbWFya29AZ21haWwuY29tIiwicm9sZSI6IkRSSVZFUiIsImlkIjo1LCJleHAiOjE2NzU2NTAzODQsImlhdCI6MTY3NTYzMjM4NH0.fIyHGCdxv91Rn7kUrgSBKyVX3btLEOwuVgB0rbsiLRNlB-eJ_8RuhOcz72Nag8hPKetvgvKh70BuhKGLHLvCVg"))
                .thenReturn(UserType.USER.name());

        Ride current_ride = mockCorrectRide_Finished();
        current_ride.setId(1);


        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("myReason");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.panic(null,rejectionTextDTO,authorization));
        assertEquals("Passenger does not exist!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }

    @Test
    public void testAddFavouriteRide_correctData(){
        int passengerId = 1;
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();

        Ride rideForFavourite = mockCorrectRide_Finished();

        favouriteRideDTO.setBabyTransport(rideForFavourite.getDriver().getVehicle().isForBabies());
        favouriteRideDTO.setPetTransport(rideForFavourite.getDriver().getVehicle().isForAnimals());
        favouriteRideDTO.setVehicleType(rideForFavourite.getDriver().getVehicle().getVehicleType().getVehicleName());
        favouriteRideDTO.setFavoriteName("My new favorite");

        List<UserRef> passenger = new ArrayList<>();
        passenger.add(new UserRef(mockCorrectPassenger()));
        favouriteRideDTO.setPassengers(passenger);

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(rideForFavourite.getRoute());
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        FavouriteRide favouriteRide = new FavouriteRide();
        favouriteRide.setId(1);
        favouriteRide.setFavoriteName("My new favorite");
        favouriteRide.setBabyTransport(favouriteRideDTO.isBabyTransport());
        favouriteRide.setPetTransport(favouriteRideDTO.isPetTransport());
        favouriteRide.setVehicleType(favouriteRideDTO.getVehicleType());
        favouriteRide.setPassengers(new ArrayList<>());
        favouriteRide.getPassengers().add(mockCorrectPassenger());
        favouriteRide.setRoute(rideForFavourite.getRoute());


        Mockito.when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(mockCorrectPassenger()));
        Mockito.when(favouriteRideRepository.save(any(FavouriteRide.class))).thenReturn(favouriteRide);

        FavouriteRideInfoDTO favouriteRideInfoDTO = rideService.addFavouriteRide(favouriteRideDTO);
        assertEquals(routeForCreateRideDTO.getDeparture().getAddress(),favouriteRideInfoDTO.getLocations().get(0).getDeparture().getAddress());
        assertEquals(favouriteRideDTO.getFavoriteName(),favouriteRideInfoDTO.getFavoriteName());
        assertEquals(favouriteRideDTO.getVehicleType(),favouriteRideInfoDTO.getVehicleType());
        assertEquals(favouriteRideDTO.getPassengers().get(0).getId(),favouriteRideInfoDTO.getPassengers().get(0).getId());
        assertEquals(favouriteRideDTO.isBabyTransport(),favouriteRideInfoDTO.isBabyTransport());
        assertEquals(favouriteRideDTO.isPetTransport(),favouriteRideInfoDTO.isPetTransport());
    }




    @Test
    public void testAddFavouriteRide_invalidData_noPassengers(){
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();

        Ride rideForFavourite = mockCorrectRide_Finished();

        favouriteRideDTO.setBabyTransport(rideForFavourite.getDriver().getVehicle().isForBabies());
        favouriteRideDTO.setPetTransport(rideForFavourite.getDriver().getVehicle().isForAnimals());
        favouriteRideDTO.setVehicleType(rideForFavourite.getDriver().getVehicle().getVehicleType().getVehicleName());
        favouriteRideDTO.setFavoriteName("My new favorite");
        favouriteRideDTO.setPassengers(new ArrayList<>());

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(rideForFavourite.getRoute());
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.addFavouriteRide(favouriteRideDTO));
        assertEquals("Favorite route must consist of at least one passenger!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }

    @Test
    public void testAddFavouriteRide_invalidData_nullRequestBody(){
        FavouriteRideDTO favouriteRideDTO = null;


        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.addFavouriteRide(favouriteRideDTO));
        assertEquals("You cannot use a null input for request body!", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatusCode());
    }

    @Test
    public void testAddFavouriteRide_incorrectData_noSuchPassenger(){
        int passengerId = 1;
        int passengerId2 = 2;
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();

        Ride rideForFavourite = mockCorrectRide_Finished();

        favouriteRideDTO.setBabyTransport(rideForFavourite.getDriver().getVehicle().isForBabies());
        favouriteRideDTO.setPetTransport(rideForFavourite.getDriver().getVehicle().isForAnimals());
        favouriteRideDTO.setVehicleType(rideForFavourite.getDriver().getVehicle().getVehicleType().getVehicleName());
        favouriteRideDTO.setFavoriteName("My new favorite");

        Passenger passenger2 = mockCorrectPassenger();
        passenger2.setId(2);

        List<UserRef> passenger = new ArrayList<>();
        passenger.add(new UserRef(mockCorrectPassenger()));
        passenger.add(new UserRef(passenger2));
        favouriteRideDTO.setPassengers(passenger);

        Mockito.when(passengerRepository.findById(passengerId)).thenReturn(Optional.ofNullable(mockCorrectPassenger()));
        Mockito.when(passengerRepository.findById(passengerId2)).thenReturn(Optional.empty());

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(rideForFavourite.getRoute());
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()->rideService.addFavouriteRide(favouriteRideDTO));
        assertEquals("Passenger Not Found!", exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND,exception.getStatusCode());
    }
}
