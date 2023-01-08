package com.example.topgoback.Rides.Service;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideDTO;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.FavouriteRides.Model.FavouriteRide;
import com.example.topgoback.FavouriteRides.Repository.FavouriteRideRepository;
import com.example.topgoback.GeoLocations.DTO.GeoLocationDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.GeoLocations.Repository.GeoLocationRepository;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.RejectionLetters.Model.RejectionLetter;
import com.example.topgoback.RejectionLetters.Repository.RejectionLetterRepository;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Rides.Repository.RideRepository;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Routes.Repository.RouteRepository;
import com.example.topgoback.Tools.DistanceCalculator;
import com.example.topgoback.Tools.JwtTokenUtil;
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
    @Autowired
    private RejectionLetterRepository rejectionLetterRepository;
    @Autowired
    FavouriteRideRepository favouriteRideRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    public void addOne(Ride ride) {
        rideRepository.save(ride);
    }


    public UserRidesListDTO findRidesByUserId(int userId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        User user = userService.findOne(userId);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndBeginBetween(user.getId(), beginDateTimeInterval, endDateTimeInterval, pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;

    }

    public UserRidesListDTO findRidesByPassengerId(int passengerId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        Passenger passenger = passengerService.findById(passengerId);
        Page<Ride> rides = rideRepository.findByPassengerAndBeginBetween(passenger.getId(), beginDateTimeInterval, endDateTimeInterval, pageable);
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


        route.setLenght((float) DistanceCalculator.getDistanceFromLocations(createRideDTO.getLocations().get(0).getDeparture(),
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
        for (RidePassengerDTO passengerDTO : createRideDTO.getPassengers()
        ) {
            Optional<Passenger> passenger = passengerRepository.findById(passengerDTO.getId());
            if (passenger.isEmpty())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger does not exist!");
            ride.getPassenger().add(passenger.get());
            UserRef userRef = new UserRef();
            userRef.setId(passenger.get().getId());
            userRef.setEmail(passenger.get().getEmail());

        }


        Driver driver = this.DriverSelection(ride, DistanceCalculator.getEstimatedTimeInMinutes(60, ride.getRoute().getLenght()));
        if (driver == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No avaliable drivers at this moment");

        ride.setDriver(driver);
        UserRef driverRef = new UserRef();
        driverRef.setId(driver.getId());
        driverRef.setEmail(driver.getEmail());


        rideRepository.save(ride);
        RideDTO response = new RideDTO(ride);
        return response;


    }

    private Driver DriverSelection(Ride ride, float estimatedTime) {
        List<Driver> drivers = driverRepository.findAll();
        List<Driver> viableDrivers = new ArrayList<Driver>();
        for (Driver driver : drivers
        ) {
            Vehicle vehicle = driver.getVehicle();
            if (!driver.isActive()) continue;
            if (!vehicle.getVehicleType().getVehicleName().equals(ride.getVehicleName().toString()))
                continue;
            if (!vehicle.isForBabies()) {
                if (ride.isForBabies()) continue;
            }
            if (!vehicle.isForAnimals()) {
                if (ride.isForAnimals()) continue;
            }
            if (this.checkForActiveRide(driver)) continue;
            if (this.checkForAcceptedRide(driver, estimatedTime)) continue;
            viableDrivers.add(driver);

        }
        double minimumDistance = Double.MAX_VALUE;
        Driver bestDriver = null;
        for (Driver driver : viableDrivers) {
            Vehicle vehicle = driver.getVehicle();
            double distance = DistanceCalculator.getDistanceFromLocations(new GeoLocationDTO(vehicle.getCurrentLocation()), new GeoLocationDTO(ride.getRoute().getStart()));
            if (distance < minimumDistance) {
                minimumDistance = distance;
                bestDriver = driver;
            }


        }

        return bestDriver;
    }

    private boolean checkForAcceptedRide(Driver driver, float estimatedTime) {
        List<Ride> rides = rideRepository.findRidesByDriveridAndIsAccepted(driver.getId());

        if (rides.isEmpty()) return false;
        LocalDateTime currentTime = LocalDateTime.now();
        for (Ride ride : rides) {
            if (currentTime.plusMinutes((long) estimatedTime).isAfter(ride.getStart())) return true;
        }
        return false;
    }

    private boolean checkForActiveRide(Driver driver) {
        List<Ride> ride = rideRepository.findRidesByDriveridAndIsActive(driver.getId());
        if (ride.isEmpty()) return false;
        return true;

    }


    public RideDTO acceptRide(int id) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        }

        Ride ride = optionalRide.get();
        return ChangeRideStatus(ride, Status.ACCEPTED, "Cannot accept a ride that is not in status PENDING!");
    }

    public RideDTO cancelRide(Integer id, RejectionTextDTO reason) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        }

        Ride ride = optionalRide.get();
        if (ride.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel a ride that is not in status PENDING!");
        }
        ride.setStatus(Status.REJECTED);
        RejectionLetter rejectionLetter = new RejectionLetter();

        rejectionLetter.setTimeOfRejection(LocalDateTime.now());
        rejectionLetter.setReason(reason.getReason());
        rejectionLetter.setRide(ride);

        rejectionLetterRepository.save(rejectionLetter);
        ride.setRejectionLetter(rejectionLetter);
        rideRepository.save(ride);
        return new RideDTO(ride);

    }

    private RideDTO ChangeRideStatus(Ride ride, Status status, String badRequest) {
        if (ride.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, badRequest);
        }
        ride.setStatus(status);
        rideRepository.save(ride);
        return new RideDTO(ride);
    }

    public RideDTO getDriverActiveRide(Integer driverId) {
        List<Ride> activeRides = rideRepository.findRidesByDriveridAndIsActive(driverId);
        if (activeRides.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Active ride does not exist!");

        return new RideDTO(activeRides.get(0));

    }

    public RideDTO getPassengerActiveRide(Integer passengerId) {
        List<Ride> activeRides = rideRepository.findRidesByPassengeridAndIsActive(passengerId);
        if (activeRides.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Active ride does not exist!");

        return new RideDTO(activeRides.get(0));

    }

    public RideDTO getRideById(Integer id) {
        Optional<Ride> ride = rideRepository.findById(id);
        if (ride.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        return new RideDTO(ride.get());
    }

    public RideDTO withdrawRide(Integer id) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        }

        Ride ride = optionalRide.get();

        if (ride.getStatus() != Status.PENDING && ride.getStatus() != Status.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel a ride that is not in status PENDING or STARTED!");
        }
        ride.setStatus(Status.CANCELED);
        rideRepository.save(ride);
        return new RideDTO(ride);
    }


    public PanicDTO panic(Integer id, RejectionTextDTO reason) {

        return new PanicDTO();
    }

    public RideDTO endRide(Integer id) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        }

        Ride ride = optionalRide.get();

        if (ride.getStatus() != Status.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot end a ride that is not in status FINISHED!");
        }
        ride.setStatus(Status.FINISHED);
        rideRepository.save(ride);
        return new RideDTO(ride);
    }

    public RideDTO startRide(Integer id) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        }

        Ride ride = optionalRide.get();

        if (ride.getStatus() != Status.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot start a ride that is not in status ACCEPTED!");
        }
        ride.setStatus(Status.ACTIVE);
        rideRepository.save(ride);
        return new RideDTO(ride);
    }

    public FavouriteRideInfoDTO addFavouriteRide(FavouriteRideDTO ride) {
        FavouriteRide favouriteRide = new FavouriteRide();

        favouriteRide.setBabyTransport(ride.isBabyTransport());
        favouriteRide.setPetTransport(ride.isPetTransport());
        favouriteRide.setVehicleType(ride.getVehicleType());
        favouriteRide.setFavoriteName(ride.getFavoriteName());

        favouriteRide.setPassengers(new ArrayList<>());
        for (UserRef p : ride.getPassengers()
        ) {
            Optional<Passenger> passenger = passengerRepository.findById(p.getId());
            if (passenger.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passenger Not Found!");
            favouriteRide.getPassengers().add(passenger.get());
        }
        Route route = new Route();

        GeoLocation departure = new GeoLocation(ride.getLocations().get(0).getDeparture());
        GeoLocation destination = new GeoLocation(ride.getLocations().get(0).getDestination());

        route.setStart(departure);
        geoLocationRepository.save(departure);
        route.setFinish(destination);
        geoLocationRepository.save(destination);

        routeRepository.save(route);
        favouriteRide.setRoute(route);
        favouriteRideRepository.save(favouriteRide);
        return new FavouriteRideInfoDTO(favouriteRide);


    }

    public List<FavouriteRideInfoDTO> getFavouriteRides(String authorization) {
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int id = jwtTokenUtil.getUserIdFromToken(jwtToken);
        List<FavouriteRide> fav = favouriteRideRepository.findByPassengerId(id);
        List<FavouriteRideInfoDTO> response = new ArrayList<>();
        for (FavouriteRide fr:fav
             ) {
            response.add(new FavouriteRideInfoDTO(fr));

        }
        return response;
    }

    public void deleteFavouriteRides(Integer id) {
        Optional<FavouriteRide> fr = favouriteRideRepository.findById(id);
        if (fr.isEmpty()) throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Favorite location does not exist!");
        favouriteRideRepository.delete(fr.get());
    }
}
