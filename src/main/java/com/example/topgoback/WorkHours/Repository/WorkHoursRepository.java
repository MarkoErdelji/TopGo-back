package com.example.topgoback.WorkHours.Repository;

import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.WorkHours.Model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkHoursRepository extends JpaRepository<WorkHours,Integer> {
    public List<WorkHours> findByDriver(Driver driver);
}
