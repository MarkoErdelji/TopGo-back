package com.example.topgoback.Rides.Controller;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:controller-test.properties")

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

}
