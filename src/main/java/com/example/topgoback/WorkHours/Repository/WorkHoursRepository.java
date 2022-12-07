package com.example.topgoback.WorkHours.Repository;

import com.example.topgoback.WorkHours.Model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkHoursRepository extends JpaRepository<WorkHours,Integer> {
    @Query("Select w from WorkHours w where w.driver.id=?1")
    public List<WorkHours> getWorkHoursByDriverId(int driverId);
}
