package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Routes.Repository.RouteRepository;
import com.example.topgoback.Routes.Service.RouteService;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Users.DTO.RidePassengerDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Service.UnregisteredUserService;
import com.example.topgoback.Users.Service.UserService;
import com.example.topgoback.Vehicles.Model.Vehicle;
import com.example.topgoback.Vehicles.Repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    GeoLocationRepository geoLocationRepository;
    @Autowired
    DriverRepository driverRepository;

    @Autowired
    private UserService userService;

    public void addOne(Ride ride) { rideRepository.save(ride);}




    public UserRidesListDTO findRidesByUserId(int userId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        User user = userService.findOne(userId);
        System.out.println("SELECT r FROM Ride r JOIN r.passenger p JOIN r.driver d WHERE p.id = " + userId + " OR d.id = " + userId + " AND r.start BETWEEN " + beginDateTimeInterval + " AND " + endDateTimeInterval);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndBeginBetween(user.getId(),beginDateTimeInterval,endDateTimeInterval,pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;

        }
    public UserRidesListDTO findRidesByPassengerId(int passengerId) {

        UserRidesListDTO userRidesListDTO = new UserRidesListDTO();
        userRidesListDTO.setTotalCount(243);
        ArrayList<UserRideDTO> userRides = new ArrayList<>();
        userRides.add(UserRideDTO.getMockupData());
        userRidesListDTO.setResults(userRides);

        return userRidesListDTO;
    }

    public RideDTO createRide(CreateRideDTO createRideDTO) {
        Ride ride = new Ride();
        RideDTO response = new RideDTO();


        ride.setForBabies(createRideDTO.getBabyTransport());
        response.setBabyTransport(createRideDTO.getBabyTransport());
        ride.setForAnimals(createRideDTO.getPetTransport());
        response.setPetTransport(createRideDTO.getPetTransport());

        Route route = new Route();

        GeoLocation departure = new GeoLocation(createRideDTO.getLocations().get(0).getDeparture());
        GeoLocation destination = new GeoLocation(createRideDTO.getLocations().get(0).getDestination());

        route.setStart(departure);
        geoLocationRepository.save(departure);
        route.setFinish(destination);
        geoLocationRepository.save(destination);


        response.setLocations(createRideDTO.getLocations());
        route.setLenght((float)DistanceCalculator.getDistanceFromLocations(createRideDTO.getLocations().get(0).getDeparture(),
                createRideDTO.getLocations().get(0).getDestination()));
        routeRepository.save(route);

        ride.setRoute(route);
        ride.setStart(LocalDateTime.now());
        response.setStartTime(LocalDateTime.now());

        ride.setStatus(Status.PENDING);
        response.setStatus(Status.PENDING);

        ride.setVehicleName(createRideDTO.getVehicleType());
        response.setVehicleType(createRideDTO.getVehicleType());
        float totalCost = DistanceCalculator.getPrice(route.getLenght(),
                vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType()));

        ride.setPrice(totalCost);
        response.setTotalCost(totalCost);

        response.setEstimatedTimeInMinutes(DistanceCalculator.getEstimatedTimeInMinutes(60,route.getLenght()));


        ride.setPassenger(new ArrayList<Passenger>());
        response.setPassengers(new ArrayList<UserRef>());
        for (RidePassengerDTO passengerDTO: createRideDTO.getPassengers()
             ) {
            Optional<Passenger> passenger = passengerRepository.findById(passengerDTO.getId());
            if (passenger.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Passenger does not exist!");
            ride.getPassenger().add(passenger.get());
            UserRef userRef = new UserRef();
            userRef.setId(passenger.get().getId());
            userRef.setEmail(passenger.get().getEmail());
            response.getPassengers().add(userRef);

        }

        rideRepository.save(ride);
        response.setId(ride.getId());
        Driver driver = this.DriverSelection(ride);
        return response;


    }

    private Driver DriverSelection(Ride ride) {
        List<Driver> drivers = driverRepository.findAll();
        List<Driver> viableDrivers = new ArrayList<Driver>();
        for (Driver driver:drivers
             ) {
            Vehicle vehicle = driver.getVehicle();
            if (!vehicle.getVehicleType().getVehicleName().equals(ride.getVehicleName().toString()))
                continue;
            if(!vehicle.isForBabies())
            {
                if (ride.isForBabies()) continue;
            }
            if(!vehicle.isForAnimals())
            {
                if (ride.isForAnimals()) continue;
            }
            if(this.checkForActiveRide(driver))continue;







        }


    }

    private boolean checkForActiveRide(Driver driver) {

        return false;

    }
}
