package com.example.topgoback.Rides.Repository;

import com.example.topgoback.Enums.Status;
import com.example.topgoback.Enums.VehicleName;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(9, rides.getNumberOfElements());
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
    void findRidesByDriveridAndIsActiveWithValidInputReturnsExpectedResult() {
        int driverId = 4;
        List<Ride> rides = rideRepository.findRidesByDriveridAndIsActive(driverId);

        assertEquals(1, rides.size());
        assertEquals(Status.ACTIVE, rides.get(0).getStatus());
    }
    @Test
    public void testFindRidesByDriveridAndIsActiveNoRidesFound() {
        int driverId = 6;

        List<Ride> rides = rideRepository.findRidesByDriveridAndIsActive(driverId);

        assertTrue(rides.isEmpty(), "No rides should be found for this driver and status");
    }
    @Test
    void findRidesByDriveridAndIsAcceptedWithValidInputReturnsExpectedResult() {
        int driverId = 4;
        List<Ride> rides = rideRepository.findRidesByDriveridAndIsAccepted(driverId);

        assertEquals(1, rides.size());
        assertEquals(Status.ACCEPTED, rides.get(0).getStatus());
    }
    @Test
    public void testFindRidesByDriveridAndIsAcceptedNoRidesFound() {
        int driverId = 6;

        List<Ride> rides = rideRepository.findRidesByDriveridAndIsAccepted(driverId);

        assertTrue(rides.isEmpty(), "No rides should be found for this driver and status");
    }
    @Test
    void findRidesByDriveridAndIsFinishedWithValidInputReturnsExpectedResult() {
        int driverId = 4;
        List<Ride> rides = rideRepository.findRidesByDriveridAndIsFinished(driverId);

        assertEquals(2, rides.size());
        assertEquals(Status.FINISHED, rides.get(0).getStatus());
    }
    @Test
    public void testFindRidesByDriveridAndIsFinishedNoRidesFound() {
        int driverId = 5;

        List<Ride> rides = rideRepository.findRidesByDriveridAndIsFinished(driverId);

        assertTrue(rides.isEmpty(), "No rides should be found for this driver and status");
    }
    @Test
    public void testFindRidesByStatus() {
        List<Ride> rides = rideRepository.findRidesByStatus(Status.ACTIVE);
        assertEquals(1, rides.size());

        Ride ride = rides.get(0);
        assertEquals(4, ride.getId());
        assertEquals("2023-01-01T15:00", ride.getEnd().toString());
        assertEquals(true, ride.isForAnimals());
        assertEquals(true, ride.isForBabies());
        assertEquals(false, ride.isPanic());
        assertEquals(1433.00, ride.getPrice());
        assertEquals("2023-01-01T14:00", ride.getStart().toString());
        assertEquals(Status.ACTIVE, ride.getStatus());
        assertEquals(VehicleName.VAN, ride.getVehicleName());
    }


}
