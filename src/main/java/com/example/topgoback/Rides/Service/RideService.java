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
import com.example.topgoback.Panic.Model.Panic;
import com.example.topgoback.Panic.Repository.PanicRepository;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
import com.example.topgoback.RejectionLetters.Model.RejectionLetter;
import com.example.topgoback.RejectionLetters.Repository.RejectionLetterRepository;
import com.example.topgoback.Rides.Controller.CreateRideHandler;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private SimpMessagingTemplate messagingTemplate;
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
    PanicRepository panicRepository;
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
    public UserRidesListDTO findRidesByDriversId(int driversId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval) {

        Optional<Driver> driver = driverRepository.findById(driversId);
        if(driver.isEmpty()){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }

        Page<Ride> rides = rideRepository.findByDriverAndBeginBetween(driver.get().getId(), beginDateTimeInterval, endDateTimeInterval, pageable);
        List<UserRideDTO> userRideDTOList = UserRideDTO.convertToUserRideDTO(rides.getContent());
        UserRidesListDTO userRidesListDTO = new UserRidesListDTO(new PageImpl<>(userRideDTOList, pageable, rides.getTotalElements()));
        userRidesListDTO.setTotalCount((int) rides.getTotalElements());

        return userRidesListDTO;
    }

    public RideDTO findRideByPassengerAndIsPending(int passengerId){
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsPending(passengerId);
        return new RideDTO(rides.get(0));
    }

    public RideDTO getPassengersAcceptedRide(int passengerId){
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsAccepted(passengerId);
        return new RideDTO(rides.get(0));
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
        if (driver == null) {
            sendNoMoreDriversUpdateToPassenger(createRideDTO.getPassengers());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No avaliable drivers at this moment");
        }

        ride.setDriver(driver);
        UserRef driverRef = new UserRef();
        driverRef.setId(driver.getId());
        driverRef.setEmail(driver.getEmail());


        rideRepository.save(ride);
        RideDTO response = new RideDTO(ride);
        WebSocketSession webSocketSession = CreateRideHandler.driverSessions.get(response.getDriver().getId().toString());
        if(webSocketSession != null) {
            CreateRideHandler.notifyDriverAboutCreatedRide(webSocketSession,response);
        }
        else {
            sendDriverRideUpdate(response);
        }
        return response;


    }

    @CrossOrigin(origins = "http://localhost:4200")
    public void sendDriverRideUpdate(RideDTO update) {
        messagingTemplate.convertAndSend("/topic/driver/ride/"+update.driver.getId(), update);
    }


    public void sendNoMoreDriversUpdateToPassenger(List<RidePassengerDTO> passengers){
        List<WebSocketSession> sessions = new ArrayList<>();
        for(RidePassengerDTO p:passengers){
            WebSocketSession webSocketSession = CreateRideHandler.passengerSessions.get(p.getId().toString());
            if(webSocketSession != null){
                sessions.add(webSocketSession);
            }
        }
        if(!sessions.isEmpty()) {
            CreateRideHandler.notifyPassengerAboutNoDriversLeft(sessions,new Error());
        }
        sendPassengerNoDriversLeftUpdate(passengers);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    public void sendPassengerNoDriversLeftUpdate(List<RidePassengerDTO> passengers) {
        for(RidePassengerDTO p: passengers){
            messagingTemplate.convertAndSend("/topic/passenger/ride/"+p.getId(), "No drivers left!");
        }
    }

    private Driver DriverSelection(Ride ride, float estimatedTime) {
        List<Driver> drivers = driverRepository.findAll();
        List<Ride> rides = rideRepository.findRidesByStatus(Status.REJECTED);
        List<Driver> viableDrivers = new ArrayList<Driver>();
        for (Driver driver : drivers
        ) {
            boolean hasRejected = false;
            for (Ride rejectedRide: rides){
                if(Objects.equals(rejectedRide.getDriver().getId(), driver.getId()) && arePassengerListsEqual(rejectedRide.getPassenger(),ride.getPassenger())
                   && (ride.getStart().isAfter(LocalDateTime.now().minusMinutes(20)) && ride.getStart().isBefore(LocalDateTime.now()))) {
                    hasRejected = true;
                    break;
                }
            }
            if(hasRejected){
                continue;
            }
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


    public static boolean arePassengerListsEqual(List<Passenger> list1, List<Passenger> list2) {
        Set<Integer> set1 = list1.stream().map(Passenger::getId).collect(Collectors.toSet());
        Set<Integer> set2 = list2.stream().map(Passenger::getId).collect(Collectors.toSet());
        return set1.equals(set2);
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
        if (ride.getStatus() != Status.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel a ride that is not in status ACCEPTED!");
        }
        ride.setStatus(Status.CANCELED);
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
    public RideDTO getDriverAcceptedRide(Integer driverId) {
        List<Ride> activeRides = rideRepository.findRidesByDriveridAndIsAccepted(driverId);
        if (activeRides.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Active ride does not exist!");

        return new RideDTO(activeRides.get(0));
    }
    public List<RideDTO> getDriverFinishedRides(Integer driverId) {
        List<Ride> finishedRides = rideRepository.findRidesByDriveridAndIsFinished(driverId);
        List<RideDTO> finishedRidesDTOS = new ArrayList<RideDTO>();
        if (finishedRides.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Active ride does not exist!");
        for(Ride ride: finishedRides){
            finishedRidesDTOS.add(new RideDTO(ride));
        }

        return finishedRidesDTOS;
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


    public PanicDTO panic(Integer id, RejectionTextDTO reason, String authorization) {
        String jwtToken = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            jwtToken = authorization.substring(7);
        }
        int userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        String type = jwtTokenUtil.getRoleFromToken(jwtToken);

        Panic panic = new Panic();
        if (type.equals("USER")) panic.setUser(passengerRepository.findById(userId).get());
        if (type.equals("DRIVER")) panic.setUser(driverRepository.findById(userId).get());
        System.out.print(jwtToken);
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Ride does not exist!");
        Ride ride = optionalRide.get();
        if (ride.getStatus() != Status.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot panic on a ride than is not ACTIVE!");
        }
        ride.setStatus(Status.PANIC);
        rideRepository.save(ride);
        panic.setRide(ride);
        panic.setTime(LocalDateTime.now());
        panic.setReason(reason.getReason());
        panicRepository.save(panic);




        return new PanicDTO(panic);
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
        ride.setEnd(LocalDateTime.now());
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


    public RideDTO declineRide(Integer id, RejectionTextDTO reason) {
        Optional<Ride> optionalRide = rideRepository.findById(id);
        if (optionalRide.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride does not exist!");
        }

        Ride ride = optionalRide.get();
        if (ride.getStatus() != Status.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot decline a ride that is not in status PENDING!");
        }
        ride.setStatus(Status.REJECTED);

        RejectionLetter rejectionLetter = new RejectionLetter();
        rejectionLetter.setTimeOfRejection(LocalDateTime.now());
        rejectionLetter.setReason(reason.getReason());
        rejectionLetter.setRide(ride);

        rejectionLetterRepository.save(rejectionLetter);
        ride.setRejectionLetter(rejectionLetter);

        rideRepository.save(ride);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        createRideDTO.setBabyTransport(ride.isForBabies());
        createRideDTO.setPetTransport(ride.isForAnimals());
        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(ride.getRoute());
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        createRideDTO.setLocations(routeForCreateRideDTOS);
        createRideDTO.setVehicleType(ride.getVehicleName());
        List<RidePassengerDTO> ridePassengerDTOS = new ArrayList<>();
        for(Passenger p:ride.getPassenger()){
            RidePassengerDTO ridePassengerDTO = new RidePassengerDTO();
            ridePassengerDTO.setEmail(p.getEmail());
            ridePassengerDTO.setId(p.getId());
            ridePassengerDTOS.add(ridePassengerDTO);
        }
        createRideDTO.setPassengers(ridePassengerDTOS);

        RideDTO newRide = createRide(createRideDTO);
        return newRide;

    }
}
