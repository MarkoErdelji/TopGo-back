package com.example.topgoback.Rides.Repository;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Rides.Model.Ride;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class RideRepositoryTestEmbedded {

    @Autowired
    private RideRepository rideRepository;


    @Test
    public void testFindByDriverOrPassengerAndStartBetween() {
        int userId = 4;
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-01-02T00:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(userId, startDate, endDate, pageable);
        assertEquals(10, rides.getNumberOfElements());
    }


    @Test
    void testFindByDriverOrPassengerAndStartBetweenWithInvalidUserId() {
        int invalidUserId = 0;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 2, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(invalidUserId, startDate, endDate, pageable);
        assertTrue(rides.isEmpty());
    }

    @Test
    void testFindByDriverOrPassengerAndStartBetweenWithInvalidDateRange() {
        int userId = 4;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 1, 2, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(userId, startDate, endDate, pageable);
        assertTrue(rides.isEmpty());
    }

    @Test
    public void testFindRidesByPassengerIdAndIsActive() {
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsActive(21);
        assertEquals(1, rides.size());
        assertEquals(Status.ACTIVE, rides.get(0).getStatus());
    }

    @Test
    void testFindRidesByPassengerIdAndIsActive_InvalidPassengerId() {
        int invalidPassengerId = -1;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsActive(invalidPassengerId);
        assertTrue(rides.isEmpty());
    }

    @Test
    void testFindRidesByPassengerIdAndIsActive_NoActiveRides() {
        int validPassengerId = 22;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsActive(validPassengerId);
        assertTrue(rides.isEmpty());
    }

    @Test
    void testFindRidesByPassengerIdAndIsActive_MultipleActiveRides() {
        int validPassengerId = 23;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsActive(validPassengerId);
        assertFalse(rides.isEmpty());
        assertEquals(2, rides.size());
    }

    @Test
    public void findRidesByPassengeridAndIsAccepted() {
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsAccepted(21);
        assertEquals(1, rides.size());
        assertEquals(Status.ACCEPTED, rides.get(0).getStatus());
    }

    @Test
    void testFindRidesByPassengerIdAndIsAccepted_InvalidPassengerId() {
        int invalidPassengerId = -1;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsAccepted(invalidPassengerId);
        assertTrue(rides.isEmpty());
    }


    @Test
    public void testFindRidesByPassengeridAndIsAccepted_NoRidesFound() {
        int passengerId = 22;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsAccepted(passengerId);
        assertEquals(0, rides.size());
    }

    @Test
    public void testFindRidesByPassengeridAndIsAccepted_MultipleRidesFound() {
        int passengerId = 23;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsAccepted(passengerId);
        assertEquals(2, rides.size());

    }

    @Test
    public void findRidesByPassengeridAndIsPending() {
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsPending(21);
        assertEquals(1, rides.size());
        assertEquals(Status.PENDING, rides.get(0).getStatus());
    }

    @Test
    void testFindRidesByPassengerIdAndIsPending_InvalidPassengerId() {
        int invalidPassengerId = -1;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsPending(invalidPassengerId);
        assertTrue(rides.isEmpty());
    }


    @Test
    public void testFindRidesByPassengeridAndIsPending_NoRidesFound() {
        int passengerId = 22;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsPending(passengerId);
        assertEquals(0, rides.size());
    }

    @Test
    public void testFindRidesByPassengeridAndIsPending_MultipleRidesFound() {
        int passengerId = 23;
        List<Ride> rides = rideRepository.findRidesByPassengeridAndIsPending(passengerId);
        assertEquals(2, rides.size());

    }

}
