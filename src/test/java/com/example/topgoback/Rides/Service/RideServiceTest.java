package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideDTO;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.FavouriteRides.Repository.FavouriteRideRepository;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.Panic.Repository.PanicRepository;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.RejectionLetters.Repository.RejectionLetterRepository;
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
