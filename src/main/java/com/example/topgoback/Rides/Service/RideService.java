package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Routes.Repository.RouteRepository;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Users.DTO.RidePassengerDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Model.Passenger;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.Users.Repository.PassengerRepository;
import com.example.topgoback.Users.Service.PassengerService;
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
import java.util.Collection;
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

    @Autowired
    private PassengerService passengerService;

    public void addOne(Ride ride) { rideRepository.save(ride);}




    public UserRidesListDTO findRidesByUserId(int userId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        User user = userService.findOne(userId);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndBeginBetween(user.getId(),beginDateTimeInterval,endDateTimeInterval,pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;

        }

    public UserRidesListDTO findRidesByPassengerId(int passengerId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        Passenger passenger = passengerService.findById(passengerId);
        Page<Ride> rides = rideRepository.findByPassengerAndBeginBetween(passenger.getId(),beginDateTimeInterval,endDateTimeInterval,pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;
    }


    public RideDTO createRide(CreateRideDTO createRideDTO) {
        Ride ride = new Ride();


        ride.setForBabies(createRideDTO.getBabyTransport());
        ride.setForAnimals(createRideDTO.getPetTransport());

        Route route = new Route();

        GeoLocation departure = new GeoLocation(createRideDTO.getLocations().get(0).getDeparture());
        GeoLocation destination = new GeoLocation(createRideDTO.getLocations().get(0).getDestination());

        route.setStart(departure);
        geoLocationRepository.save(departure);
        route.setFinish(destination);
        geoLocationRepository.save(destination);


        route.setLenght((float)DistanceCalculator.getDistanceFromLocations(createRideDTO.getLocations().get(0).getDeparture(),
                createRideDTO.getLocations().get(0).getDestination()));
        routeRepository.save(route);

        ride.setRoute(route);
        ride.setStart(LocalDateTime.now());

        ride.setStatus(Status.PENDING);

        ride.setVehicleName(createRideDTO.getVehicleType());
        float totalCost = DistanceCalculator.getPrice(route.getLenght(),
                vehicleTypeRepository.findByVehicleName(createRideDTO.getVehicleType()));

        ride.setPrice(totalCost);


        ride.setPassenger(new ArrayList<Passenger>());
        for (RidePassengerDTO passengerDTO: createRideDTO.getPassengers()
             ) {
            Optional<Passenger> passenger = passengerRepository.findById(passengerDTO.getId());
            if (passenger.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Passenger does not exist!");
            ride.getPassenger().add(passenger.get());
            UserRef userRef = new UserRef();
            userRef.setId(passenger.get().getId());
            userRef.setEmail(passenger.get().getEmail());

        }


        Driver driver = this.DriverSelection(ride,DistanceCalculator.getEstimatedTimeInMinutes(60,ride.getRoute().getLenght()));
        if (driver == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No avaliable drivers at this moment");

        ride.setDriver(driver);
        UserRef driverRef = new UserRef();
        driverRef.setId(driver.getId());
        driverRef.setEmail(driver.getEmail());


        rideRepository.save(ride);
        RideDTO response = new RideDTO(ride);
        return response;


    }

    private Driver DriverSelection(Ride ride,float estimatedTime) {
        List<Driver> drivers = driverRepository.findAll();
        List<Driver> viableDrivers = new ArrayList<Driver>();
        for (Driver driver:drivers
             ) {
            Vehicle vehicle = driver.getVehicle();
            if (!driver.isActive())continue;
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
            if(this.checkForAcceptedRide(driver,estimatedTime))continue;
            viableDrivers.add(driver);

        }
        double minimumDistance = Double.MAX_VALUE;
        Driver bestDriver = null;
        for (Driver driver:viableDrivers)
        {
            Vehicle vehicle = driver.getVehicle();
            double distance = DistanceCalculator.getDistanceFromLocations(new GeoLocationDTO(vehicle.getCurrentLocation()),new GeoLocationDTO(ride.getRoute().getStart()));
            if (distance< minimumDistance)
            {
                minimumDistance = distance;
                bestDriver = driver;
            }


        }

    return bestDriver;
    }

    private boolean checkForAcceptedRide(Driver driver,float estimatedTime) {
        List<Ride> rides = rideRepository.findRidesByDriveridAndIsAccepted(driver.getId());

        if (rides.isEmpty())return false;
        LocalDateTime currentTime = LocalDateTime.now();
        for (Ride ride:rides)
        {
            if (currentTime.plusMinutes((long) estimatedTime).isAfter(ride.getStart()))return true;
        }
        return false;
    }

    private boolean checkForActiveRide(Driver driver) {
        List<Ride> ride = rideRepository.findRidesByDriveridAndIsActive(driver.getId());
        if (ride.isEmpty())return false;
        return true;

    }


    public RideDTO acceptRide(int id) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if(optionalRide.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ride does not exist!");
        }

        Ride ride = optionalRide.get();
        if(ride.getStatus() != Status.PENDING){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cannot accept a ride that is not in status PENDING!");
        }
        ride.setStatus(Status.ACCEPTED);
        rideRepository.save(ride);
        return new RideDTO(ride);
    }
}
