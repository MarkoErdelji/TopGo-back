package com.example.topgoback.WorkHours.Repository;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.WorkHours.Model.WorkHours;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkHoursRepository extends JpaRepository<WorkHours,Integer> {
    public List<WorkHours> findByDriver(Driver driver);

    @Query("SELECT w FROM WorkHours w WHERE w.startHours BETWEEN :start AND :end AND w.driver = :driver")
    List<WorkHours> findWorkHoursWithinLast24Hours(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("driver") Driver driver);
    @Query("SELECT DISTINCT wr FROM WorkHours wr INNER JOIN wr.driver d WHERE  d.id = :userId AND (wr.startHours BETWEEN :startDate AND :endDate)")
    Page<WorkHours> findByDriverAndBeginBetween(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);
}
