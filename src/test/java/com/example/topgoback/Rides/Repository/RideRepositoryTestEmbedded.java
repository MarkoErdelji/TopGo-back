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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(locations="classpath:application-test.properties")
public class RideRepositoryTestEmbedded {

    @Autowired
    private RideRepository rideRepository;


    @Test
    public void testFindByDriverOrPassengerAndStartBetweenWithDriverId_correctData_returnsPageOfRides() {
        int driverId = 4;
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-01-02T00:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(driverId, startDate, endDate, pageable);
        assertThat(rides.getTotalElements()).isEqualTo(2);
        assertThat(rides.getContent().get(0).getId()).isEqualTo(1);
    }


    @Test
    public void testFindByDriverOrPassengerAndStartBetweenWithPassengerId_correctData_returnsPageOfRides() {
        int passengerId = 21;
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-01-02T00:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(passengerId, startDate, endDate, pageable);
        assertThat(rides.getTotalElements()).isEqualTo(2);
        assertThat(rides.getContent().get(0).getId()).isEqualTo(1);
    }


    @Test
    void testFindByDriverOrPassengerAndStartBetween_incorrectUserId_returnsEmptyPage() {
        int invalidUserId = 0;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 2, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(invalidUserId, startDate, endDate, pageable);
        assertTrue(rides.isEmpty());
    }

    @Test
    void testFindByDriverOrPassengerAndStartBetween_incorrectDates_returnsEmptyPage() {
        int userId = 4;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 1, 2, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ride> rides = rideRepository.findByDriverOrPassengerAndStartBetween(userId, startDate, endDate, pageable);
        assertTrue(rides.isEmpty());
    }


    @Test
    public void testfindByPassengerAndBeginBetween_correctData_returnsPageOfRides() {
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T08:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-01-01T12:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByPassengerAndBeginBetween(21, startDate, endDate, pageable);

        assertThat(rides.getTotalElements()).isEqualTo(1);
        assertThat(rides.getContent().get(0).getId()).isEqualTo(1);
    }

    @Test
    public void testfindByPassengerAndBeginBetween_incorrectUserId_returnsEmptyPage() {
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T09:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-01-01T13:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByPassengerAndBeginBetween(999, startDate, endDate, pageable);

        assertThat(rides.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void testfindByPassengerAndBeginBetween_incorrectDates_returnsEmptyPage() {
        LocalDateTime startDate = LocalDateTime.parse("2020-01-01T09:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-01-01T15:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByPassengerAndBeginBetween(999, startDate, endDate, pageable);

        assertThat(rides.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void testfindByDriverAndBeginBetweenTest_correctData_returnsPageOfRides() {
        int userId = 4;
        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 8, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 1, 19, 0, 0);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByDriverAndBeginBetween(userId, startDate, endDate, pageable);
        assertEquals(2, rides.getTotalElements());

        List<Ride> rideList = rides.getContent();
        assertEquals(2, rideList.size());
        assertEquals(1, rideList.get(0).getId());
        assertEquals(2, rideList.get(1).getId());
    }

    @Test
    public void testFindByDriverAndBeginBetween_incorrectUserId_returnsEmptyPage() {
        int userId = 21;
        LocalDateTime startDate = LocalDateTime.parse("2023-01-01T09:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-01-01T15:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByDriverAndBeginBetween(userId, startDate, endDate, pageable);

        assertEquals(0, rides.getTotalElements());
    }

    @Test
    public void testFindByDriverAndBeginBetween_incorrectDates_returnsEmptyPage() {
        int userId = 4;
        LocalDateTime startDate = LocalDateTime.parse("2020-01-01T09:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2020-01-01T15:00:00");
        Pageable pageable = PageRequest.of(0, 10);

        Page<Ride> rides = rideRepository.findByDriverAndBeginBetween(userId, startDate, endDate, pageable);

        assertEquals(0, rides.getTotalElements());
    }
}
