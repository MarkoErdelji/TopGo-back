package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.UserType;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.FavouriteRides.Repository.FavouriteRideRepository;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Panic.Repository.PanicRepository;
import com.example.topgoback.RejectionLetters.Repository.RejectionLetterRepository;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Routes.Repository.RouteRepository;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Tools.JwtTokenUtil;
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



}
