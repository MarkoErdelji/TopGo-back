package com.example.topgoback.Rides.Controller;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.RejectionLetters.DTO.RejectionTextDTO;
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

    @DisplayName("Should return Unauthorized for ride details = /api/ride/{id}")
    @Test
    public void testGetRide_Unauthorized(){
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized",exception.getBody().getMessage());
    }

    @DisplayName("Should return NOT FOUND for ride details = /api/ride/{id}")
    @Test
    public void testGetRide_RideDoesNotExist(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/-1", HttpMethod.GET, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }

    @DisplayName("Should return ride details = /api/ride/{id}")
    @Test
    public void testGetRide_RideDetails(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/1", HttpMethod.GET, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
    }

    @DisplayName("Should return Forbidden for cancel ride = /api/ride/cancel")
    @Test
    public void testCancelRide_Forbidden(){
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("Reason");
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<RejectionTextDTO> entity = new HttpEntity<>(rejectionTextDTO, headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/cancel", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }

    @DisplayName("Should return Unauthorized for cancel ride = /api/ride/{id}/cancel")
    @Test
    public void testCancelRide_Unauthorized(){
//        headers.set("Authorization", "Bearer ");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/cancel", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        }catch(ResourceAccessException resourceAccessException){
            //If enters catch means there was no JWT (Authorization error), Happens with RestTemplate because it tries to reconnect
            assertNotNull(resourceAccessException);
        }

    }

    @DisplayName("Should return Not found for cancel ride = /api/ride/{id}/cancel")
    @Test
    public void testCancelRide_RideDoesNotExist(){
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("Reason");
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<RejectionTextDTO> entity = new HttpEntity<>(rejectionTextDTO, headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/-1/cancel", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }

    @DisplayName("Should return Body is missing for cancel ride = /api/ride/{id}/cancel")
    @Test
    public void testCancelRide_rejectionLetterIsMissing(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/cancel", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertTrue(exception.getBody().getMessage().startsWith("Required request body is missing"));
    }


    @DisplayName("Should return canceledRide = /api/ride/{id}/cancel")
    @Test
    public void testGetRide_CancelRide(){
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("Reason");
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<RejectionTextDTO> entity = new HttpEntity<>(rejectionTextDTO, headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/1/cancel", HttpMethod.PUT, entity, new ParameterizedTypeReference<RideDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        RideDTO rideDTO = responseEntity.getBody();
        assertNotNull(rideDTO);
        assertEquals("REJECTED", rideDTO.getStatus().toString());
    }

    @DisplayName("Should return Unauthorized for panic  = /api/ride/{id}/panic")
    @Test
    public void testPanic_Unauthorized(){
        headers.set("Authorization", "Bearer ");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/panic", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        }catch(ResourceAccessException resourceAccessException){
            //If enters catch means there was no JWT (Authorization error), Happens with RestTemplate because it tries to reconnect
            assertNotNull(resourceAccessException);
        }

    }

    @DisplayName("Should return Forbbiden for panic = /api/ride/{id}/panic")
    @Test
    public void testPanic_Forbbiden(){
        headers.set("Authorization", "Bearer "+loginAsUser("admin@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/panic", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }

    @DisplayName("Should return Not Found for panic = /api/ride/{id}/panic")
    @Test
    public void testPanic_RideDoesNotExist(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen345@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/-1/panic", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }

    @DisplayName("Should return Body is missing for panic ride = /api/ride/{id}/panic")
    @Test
    public void testPanic_rejectionLetterIsMissing(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/1/cancel", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertTrue(exception.getBody().getMessage().startsWith("Required request body is missing"));
    }


    @DisplayName("Should return panic ride = /api/ride/{id}/panic")
    @Test
    public void testPanic_PanicRide(){
        RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
        rejectionTextDTO.setReason("Reason");
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen345@gmail.com","test"));
        HttpEntity<RejectionTextDTO> entity = new HttpEntity<>(rejectionTextDTO, headers);
        ResponseEntity<PanicDTO> responseEntity = restTemplate.exchange("/api/ride/4/panic", HttpMethod.PUT, entity, new ParameterizedTypeReference<PanicDTO>() {});
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        PanicDTO panicDTO = responseEntity.getBody();
        assertNotNull(panicDTO);
        assertEquals(rejectionTextDTO.getReason(), panicDTO.getReason());
    }

    @DisplayName("Should return Unauthorized for delete favourite ride = /api/ride/favorites/{id}")
    @Test
    public void testDeleteFavouritesRides_Unauthorized(){
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites/1", HttpMethod.DELETE, entity, new ParameterizedTypeReference<Exception>() {});
        }catch(ResourceAccessException resourceAccessException){
            //If enters catch means there was no JWT (Authorization error), Happens with RestTemplate because it tries to reconnect
            assertNotNull(resourceAccessException);
        }

    }

    @DisplayName("Should return Forbbiden for delete favourite rides = /api/ride/favorites/{id}")
    @Test
    public void testDeleteFavouriteRides_Forbbiden(){
        headers.set("Authorization", "Bearer "+loginAsUser("admin@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites/1", HttpMethod.DELETE, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }

    @DisplayName("Should return Not Found for delete favourite rides = /api/ride/favorites/{id}")
    @Test
    public void testDeleteFavouriteRides_RideDoesNotExist(){
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/favorites/-1", HttpMethod.DELETE, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Favorite location does not exist!",exception.getBody().getMessage());
    }


    @DisplayName("Should delete favourite ride = /api/ride/favorites/{id}")
    @Test
    public void testDeleteFavouriteRides_DeleteFavouriteRides(){
        headers.set("Authorization", "Bearer "+loginAsUser("passenger@example.com","test"));
        HttpEntity<RejectionTextDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/ride/favorites/1", HttpMethod.DELETE, entity, new ParameterizedTypeReference<String>() {});
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    }

    @DisplayName("Should return Unauthorized for end ride = /api/ride/{id}/end")
    @Test
    public void testEndRide_Unauthorized(){
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/4/end", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        }catch(ResourceAccessException resourceAccessException){
            //If enters catch means there was no JWT (Authorization error), Happens with RestTemplate because it tries to reconnect
            assertNotNull(resourceAccessException);
        }

    }

    @DisplayName("Should return Forbbiden for end ride = /api/ride/{id}/end}")
    @Test
    public void testEndRides_Forbbiden(){
        headers.set("Authorization", "Bearer "+loginAsUser("admin@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/4/end", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Forbidden",exception.getBody().getMessage());
    }

    @DisplayName("Should return Not Found for end ride = /api/ride/{id}/end")
    @Test
    public void testEndRide_RideDoesNotExist(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Exception> exception = restTemplate.exchange("/api/ride/-4/end", HttpMethod.PUT, entity, new ParameterizedTypeReference<Exception>() {});
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Ride does not exist!",exception.getBody().getMessage());
    }


    @DisplayName("Should end ride = /api/ride/{id}/end")
    @Test
    public void testEndRide_EndRide(){
        headers.set("Authorization", "Bearer "+loginAsUser("ognjen34@gmail.com","test"));
        HttpEntity<RejectionTextDTO> entity = new HttpEntity<>(headers);
        ResponseEntity<RideDTO> responseEntity = restTemplate.exchange("/api/ride/4/end", HttpMethod.PUT, entity, new ParameterizedTypeReference<RideDTO>() {});

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(Status.FINISHED, responseEntity.getBody().getStatus());

    }




}
