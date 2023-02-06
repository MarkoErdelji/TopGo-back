package com.example.topgoback.Rides.Controller;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.FavouriteRides.DTO.FavouriteRideInfoDTO;
import com.example.topgoback.Rides.DTO.RideDTO;
import com.example.topgoback.Users.DTO.JWTTokenDTO;
import com.example.topgoback.Users.DTO.LoginCredentialDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

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

    @DisplayName("Should get active ride for driver - /api/ride/driver/{driverId}/active")
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

    @DisplayName("Should get active ride for driver - /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_forbidden_userJWT() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/driver/4/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }


    @DisplayName("Should get active ride for driver - /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_unauthorized() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/driver/4/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized",exception.getBody().getMessage());
    }

    @DisplayName("Should get active ride for driver - /api/ride/driver/{driverId}/active")
    @Test
    public void testGetAcceptedRideForDriver_incorrectData_NoDriver() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/driver/663246/accepted", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Active ride does not exist!",exception.getBody().getMessage());
    }
    @DisplayName("Should get active ride for passenger - /api/ride/driver/{passengerId}/active")
    @Test
    public void testGetActiveRideForPassenger_correctData_ActiveExists_PassengerJWT() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/passenger/21/active", HttpMethod.GET, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
    }

    @DisplayName("Should get forbidden - /api/ride/passenger/{passengerId}/active")
    @Test
    public void testGetActiveRideForPassenger_forbidden_userJWT() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/passenger/21/active", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }


    @DisplayName("Should get unauthorized - /api/ride/driver/{passengerId}/active")
    @Test
    public void testGetActiveRideForPassenger_unauthorized() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/passenger/21/active", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized",exception.getBody().getMessage());
    }

    @DisplayName("Should get not found - /api/ride/passenger/{passengerId}/active")
    @Test
    public void stestGetActiveRideForPassenger_incorrectData_NoDriver() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/passenger/663246/active", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Active ride does not exist!",exception.getBody().getMessage());
    }

    @DisplayName("Should start ride  - /api/ride/{rideId}/start")
    @Test
    public void testStartRide() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/2/start", HttpMethod.PUT, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
        assertEquals(rideDTO.getStatus(), Status.ACTIVE);
    }
    @DisplayName("Should get forbidden - /api/ride/{rideId}/start")
    @Test
    public void testStartRide_forbidden() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/2/start", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }
    @DisplayName("Should get unathorized - /api/ride/{rideId}/start")
    @Test
    public void testStartRide_unauthorized() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/2/start", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {
            });
        }
        catch (ResourceAccessException ex)
        {
            assertNotNull(ex);
        }
    }
    @DisplayName("Should get not found - /api/ride/{rideId}/start")
    @Test
    public void testStartRide_rideNoExist() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/50/start", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }
    @DisplayName("Should get bad request - /api/ride/{rideId}/start")
    @Test
    public void testStartRide_WrongStatus() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/4/start", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Cannot start a ride that is not in status ACCEPTED!",exception.getBody().getMessage());
    }

    @DisplayName("Should withdraw - /api/ride/{rideId}/withdraw")
    @Test
    public void testWithdrawRide() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/2/withdraw", HttpMethod.PUT, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
        assertEquals(rideDTO.getStatus(), Status.CANCELED);
    }
    @DisplayName("Should get forbidden - /api/ride/{rideId}/withdraw")
    @Test
    public void testWithdrawRide_forbidden() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/2/withdraw", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }
    @DisplayName("Should get unauthorized - /api/ride/{rideId}/withdraw")
    @Test
    public void testWithdrawRide_unauthorized() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/2/withdraw", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {
            });
        }
        catch (ResourceAccessException ex)
        {
            assertNotNull(ex);
        }
    }
    @DisplayName("Should get not found - /api/ride/{rideId}/withdraw")
    @Test
    public void testWithdrawRide_rideNoExist() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/50/withdraw", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }
    @DisplayName("Should get bad request - /api/ride/{rideId}/withdraw")
    @Test
    public void testWithdrawRide_WrongStatus() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/4/withdraw", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Cannot cancel a ride that is not in status PENDING or STARTED!",exception.getBody().getMessage());
    }

    @DisplayName("Should get favourite rides - /api/ride/favorites")
    @Test
    public void testGetFavouriteRide() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<FavouriteRideInfoDTO>> responseEntity = restTemplate.exchange("/api/ride/favorites", HttpMethod.GET, entity, new ParameterizedTypeReference<List<FavouriteRideInfoDTO>>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        List<FavouriteRideInfoDTO> favouriteRides = responseEntity.getBody();
        assertFalse(favouriteRides.isEmpty());
        assertEquals(favouriteRides.get(0).getId(), 1);
    }

    @DisplayName("Should get forbidden - /api/ride/favorites\"")
    @Test
    public void testGetFavouriteRide_forbidden() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }

    @DisplayName("Should get unauthorized - /api/ride/favorites")
    @Test
    public void testGetFavouriteRide_unauthorized() {
        headers.set("Authorization", "Bearer ");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized",exception.getBody().getMessage());
    }
    @DisplayName("Should get empty list - /api/ride/favorites")
    @Test
    public void testGetFavouriteRide_empty() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger2@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<FavouriteRideInfoDTO>> responseEntity = restTemplate.exchange("/api/ride/favorites", HttpMethod.GET, entity, new ParameterizedTypeReference<List<FavouriteRideInfoDTO>>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        List<FavouriteRideInfoDTO> favouriteRides = responseEntity.getBody();
        assertTrue(favouriteRides.isEmpty());
    }

    @DisplayName("Should accept ride  - /api/ride/{rideId}/accept")
    @Test
    public void testAcceptRide() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/1/accept", HttpMethod.PUT, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        System.out.println(responseEntity);
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
        assertEquals(rideDTO.getStatus(), Status.ACCEPTED);
    }
    @DisplayName("Should get forbidden - /api/ride/{rideId}/accept")
    @Test
    public void testAcceptRide_forbidden() {
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/start", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }
    @DisplayName("Should get unathorized - /api/ride/{rideId}/accept")
    @Test
    public void testAcceptRide_unauthorized() {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/2/accept", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {
            });
        }
        catch (ResourceAccessException ex)
        {
            assertNotNull(ex);
        }
    }
    @DisplayName("Should get not found - /api/ride/{rideId}/accept")
    @Test
    public void testAcceptRide_rideNoExist() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/50/accept", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }
    @DisplayName("Should get bad request - /api/ride/{rideId}/accept")
    @Test
    public void testAcceptRide_WrongStatus() {
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/4/accept", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Cannot accept a ride that is not in status PENDING!",exception.getBody().getMessage());
    }






}
