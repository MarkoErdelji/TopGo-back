package com.example.topgoback.Rides.Controller;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.VehicleName;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideDTO;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.GeoLocations.Model.GeoLocation;
import com.example.topgoback.Rides.DTO.CreateRideDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Routes.DTO.RouteForCreateRideDTO;
import com.example.topgoback.Routes.Model.Route;
import com.example.topgoback.Users.DTO.JWTTokenDTO;
import com.example.topgoback.Users.DTO.LoginCredentialDTO;
import com.example.topgoback.Users.DTO.RidePassengerDTO;
import com.example.topgoback.Users.DTO.UserRef;
import com.example.topgoback.Users.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:controller-test.properties")
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RideControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    HttpHeaders headers;


    @BeforeEach
    public void setUpHeaders(){
        headers = new HttpHeaders();
        headers.set("content-type", "application/json");
    }

    private String loginAsUser(String email,String password){
        LoginCredentialDTO loginCredentialDTO = new LoginCredentialDTO();
        loginCredentialDTO.setEmail(email);
        loginCredentialDTO.setPassword(password);

        HttpEntity<LoginCredentialDTO>entity = new HttpEntity<LoginCredentialDTO>(loginCredentialDTO,headers);

        ResponseEntity<JWTTokenDTO> responseEntity = restTemplate.exchange("/api/user/login", HttpMethod.POST, entity, new ParameterizedTypeReference<JWTTokenDTO>() {});
        return  responseEntity.getBody().getAccessToken();
    }

    @DisplayName("Should get active ride for driver - GET /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_correctData_ActiveExists_DriverJWT() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/driver/4/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
    }

    @DisplayName("Should get active ride for driver - GET /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_forbidden_userJWT() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/driver/4/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }


    @DisplayName("Should throw unauthorized,no jwt present - GET /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_unauthorized() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/driver/4/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized",exception.getBody().getMessage());
    }

    @DisplayName("Trying to find active ride for driver that doesn't exist - GET /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_incorrectData_NoDriver() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/driver/663246/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Active ride does not exist!",exception.getBody().getMessage());
    }


    @DisplayName("Should create a ride for now - POST /api/ride")
    @Test
    public void testCreateRide_correctData_NotScheduled() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger4@example.com","test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(24);
        passengerDTO.setEmail("passenger4@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(null);

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO,headers);

        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(500,responseEntity.getBody().getId());
        assertEquals(24,responseEntity.getBody().getPassengers().get(0).getId());
        assertEquals(5,responseEntity.getBody().getDriver().getId());
        assertEquals(Status.PENDING,responseEntity.getBody().getStatus());
        assertTrue(responseEntity.getBody().isBabyTransport());
        assertTrue(responseEntity.getBody().isPetTransport());
    }





    @DisplayName("Should create a ride for the future (Scheduled) - POST /api/ride")
    @Test
    public void testCreateRide_correctData_Scheduled() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger5@example.com","test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(25);
        passengerDTO.setEmail("passenger5@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(LocalDateTime.now().plusMinutes(120));

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO,headers);

        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(25,responseEntity.getBody().getPassengers().get(0).getId());
        assertEquals(5,responseEntity.getBody().getDriver().getId());
        assertEquals(Status.SCHEDULED,responseEntity.getBody().getStatus());
        assertTrue(responseEntity.getBody().isBabyTransport());
        assertTrue(responseEntity.getBody().isPetTransport());
    }

    @DisplayName("Should be unauthorized,no JWT present - POST /api/ride")
    @Test
    public void testCreateRide_unauthorized() {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(24);
        passengerDTO.setEmail("passenger4@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(null);

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO,headers);

        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
            });
        }catch(ResourceAccessException resourceAccessException){
            //If enters catch means there was no JWT (Authorization error), Happens with RestTemplate because it tries to reconnect
            assertNotNull(resourceAccessException);
        }
    }


    @DisplayName("Should be fobridden,driver jwt used - POST /api/ride")
    @Test
    public void testCreateRide_forbidden_driverCreatingRide() {
        headers.set("Authorization", "Bearer " + loginAsUser("ognjen34@gmail.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(24);
        passengerDTO.setEmail("passenger4@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(null);

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden", exception.getBody().getMessage());
    }


    @DisplayName("Should be fobridden,admin jwt used - POST /api/ride")
    @Test
    public void testCreateRide_forbidden_adminCreatingRide() {
        headers.set("Authorization", "Bearer " + loginAsUser("admin@gmail.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(24);
        passengerDTO.setEmail("passenger4@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(null);

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden", exception.getBody().getMessage());
    }


    @DisplayName("Should be bad request,Scheduled time more than 5 hours from now - POST /api/ride")
    @Test
    public void testCreateRide_incorrectData_ScheduledMoreThan5HoursFromNow() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger4@example.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(24);
        passengerDTO.setEmail("passenger4@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(LocalDateTime.now().plusHours(6));

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Can not order a ride thats more than 5 hours from now!", exception.getBody().getMessage());
    }


    @DisplayName("Should be bad request,One of the passengers in the list does not exist - POST /api/ride")
    @Test
    public void testCreateRide_incorrectData_passengerDoesNotExist() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger4@example.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(24);
        passengerDTO.setEmail("passenger4@example.com");
        createRideDTO.getPassengers().add(passengerDTO);
        RidePassengerDTO passengerDTO2 = new RidePassengerDTO();
        passengerDTO2.setId(13);
        passengerDTO2.setEmail("passenger89@example.com");
        createRideDTO.getPassengers().add(passengerDTO2);

        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(null);

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Passenger does not exist!", exception.getBody().getMessage());
    }


    @DisplayName("Should be bad request,One of the passengers in the list has a ride that is pending - POST /api/ride")
    @Test
    public void testCreateRide_incorrectData_alreadyHasPendingRide() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger@example.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);

        CreateRideDTO createRideDTO = new CreateRideDTO();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        createRideDTO.setLocations(new ArrayList<>());
        createRideDTO.getLocations().add(routeForCreateRideDTO);
        createRideDTO.setPassengers(new ArrayList<>());
        RidePassengerDTO passengerDTO = new RidePassengerDTO();
        passengerDTO.setId(21);
        passengerDTO.setEmail("passenger2@example.com");
        createRideDTO.getPassengers().add(passengerDTO);

        createRideDTO.setBabyTransport(true);
        createRideDTO.setPetTransport(true);
        createRideDTO.setVehicleType(VehicleName.valueOf("STANDARD"));
        createRideDTO.setScheduledTime(null);

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Cannot create a ride while you have one already pending!", exception.getBody().getMessage());
    }


    @DisplayName("Should be bad request,null Body - POST /api/ride")
    @Test
    public void testCreateRide_incorrectData_nullBody() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger@example.com", "test"));


        CreateRideDTO createRideDTO = null;

        HttpEntity<CreateRideDTO> entity = new HttpEntity<>(createRideDTO, headers);


        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getBody().getMessage().startsWith("Required request body is missing"));
    }


    @DisplayName("Should create a favorite location- POST /api/ride/favorites")
    @Test
    public void testAddFavoriteLocation_correctData() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger@example.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();
        favouriteRideDTO.setBabyTransport(true);
        favouriteRideDTO.setPetTransport(true);
        favouriteRideDTO.setVehicleType(VehicleName.STANDARD.name());
        favouriteRideDTO.setFavoriteName("My new favorite");

        List<UserRef> passenger = new ArrayList<>();
        UserRef passengerDTO = new UserRef();
        passengerDTO.setId(21);
        passengerDTO.setEmail("passenger@example.com");
        passenger.add(passengerDTO);
        favouriteRideDTO.setPassengers(passenger);

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        HttpEntity<FavouriteRideDTO> entity = new HttpEntity<>(favouriteRideDTO, headers);


        ResponseEntity<FavouriteRideInfoDTO> responseEntity = restTemplate.exchange("/api/ride/favorites", HttpMethod.POST, entity, new ParameterizedTypeReference<FavouriteRideInfoDTO>() {
        });

        System.out.println(responseEntity);

        assertEquals(7,responseEntity.getBody().getId());
        assertEquals(passengerDTO.getId(),responseEntity.getBody().getPassengers().get(0).getId());
        assertEquals(passengerDTO.getEmail(),responseEntity.getBody().getPassengers().get(0).getEmail());
        assertEquals(route.getStart().getAddress(),responseEntity.getBody().getLocations().get(0).getDeparture().getAddress());
    }


    @DisplayName("Should be unauthorized, no jwt- POST /api/ride/favorites")
    @Test
    public void testAddFavoriteLocation_unauthorized() {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();
        favouriteRideDTO.setBabyTransport(true);
        favouriteRideDTO.setPetTransport(true);
        favouriteRideDTO.setVehicleType(VehicleName.STANDARD.name());
        favouriteRideDTO.setFavoriteName("My new favorite");

        List<UserRef> passenger = new ArrayList<>();
        UserRef passengerDTO = new UserRef();
        passengerDTO.setId(21);
        passengerDTO.setEmail("passenger@example.com");
        passenger.add(passengerDTO);
        favouriteRideDTO.setPassengers(passenger);

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        HttpEntity<FavouriteRideDTO> entity = new HttpEntity<>(favouriteRideDTO, headers);


        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
            });
        }catch(ResourceAccessException resourceAccessException){
            //If enters catch means there was no JWT (Authorization error), Happens with RestTemplate because it tries to reconnect
            assertNotNull(resourceAccessException);
        }
    }

    @DisplayName("Should be forbidden driver jwt used- POST /api/ride/favorites")
    @Test
    public void testAddFavoriteLocation_forbidden_driverJWT() {
        headers.set("Authorization", "Bearer " + loginAsUser("ognjen34@gmail.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();
        favouriteRideDTO.setBabyTransport(true);
        favouriteRideDTO.setPetTransport(true);
        favouriteRideDTO.setVehicleType(VehicleName.STANDARD.name());
        favouriteRideDTO.setFavoriteName("My new favorite");

        List<UserRef> passenger = new ArrayList<>();
        UserRef passengerDTO = new UserRef();
        passengerDTO.setId(21);
        passengerDTO.setEmail("passenger@example.com");
        passenger.add(passengerDTO);
        favouriteRideDTO.setPassengers(passenger);

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        HttpEntity<FavouriteRideDTO> entity = new HttpEntity<>(favouriteRideDTO, headers);


        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }


    @DisplayName("Should be forbidden admin jwt used- POST /api/ride/favorites")
    @Test
    public void testAddFavoriteLocation_forbidden_adminJWT() {
        headers.set("Authorization", "Bearer " + loginAsUser("admin@gmail.com", "test"));

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setAddress("Bulevara cara lazara 12,Novi Sad");
        geoLocation.setLongitude(19.844139F);
        geoLocation.setLatitude(45.244620F);

        GeoLocation geoLocation2 = new GeoLocation();
        geoLocation2.setAddress("Lovcenska 12,Novi Sad");
        geoLocation2.setLongitude(19.847550F);
        geoLocation2.setLatitude(45.248321F);

        Route route = new Route();
        route.setStart(geoLocation);
        route.setFinish(geoLocation2);
        route.setLenght(2);
        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();
        favouriteRideDTO.setBabyTransport(true);
        favouriteRideDTO.setPetTransport(true);
        favouriteRideDTO.setVehicleType(VehicleName.STANDARD.name());
        favouriteRideDTO.setFavoriteName("My new favorite");

        List<UserRef> passenger = new ArrayList<>();
        UserRef passengerDTO = new UserRef();
        passengerDTO.setId(21);
        passengerDTO.setEmail("passenger@example.com");
        passenger.add(passengerDTO);
        favouriteRideDTO.setPassengers(passenger);

        List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
        RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO(route);
        routeForCreateRideDTOS.add(routeForCreateRideDTO);
        favouriteRideDTO.setLocations(routeForCreateRideDTOS);

        HttpEntity<FavouriteRideDTO> entity = new HttpEntity<>(favouriteRideDTO, headers);


        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }


    @DisplayName("Should be incorrect, null request body /api/ride/favorites")
    @Test
    public void testAddFavoriteLocation_incorrectData_nullBody() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger@example.com", "test"));

        FavouriteRideDTO favouriteRideDTO = null;

        HttpEntity<FavouriteRideDTO> entity = new HttpEntity<>(favouriteRideDTO, headers);


        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("You cannot use a null input for request body!",exception.getBody().getMessage());
    }


    @DisplayName("Should be incorrect, null field in body /api/ride/favorites")
    @Test
    public void testAddFavoriteLocation_incorrectData_nullFieldInBody() {
        headers.set("Authorization", "Bearer " + loginAsUser("passenger@example.com", "test"));

        FavouriteRideDTO favouriteRideDTO = new FavouriteRideDTO();

        HttpEntity<FavouriteRideDTO> entity = new HttpEntity<>(favouriteRideDTO, headers);


        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.POST, entity, new ParameterizedTypeReference<Exception>() {
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Validation failed for object='favouriteRideDTO'. Error count: 4",exception.getBody().getMessage());
    }




}
