package com.example.topgoback.WorkHours.Service;

import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import com.example.topgoback.WorkHours.Model.WorkHours;
import com.example.topgoback.WorkHours.Repository.WorkHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkHoursService {

    @Autowired
    WorkHoursRepository workHoursRepository;
    @Autowired
    DriverRepository driverRepository;

    public DriverWorkHoursDTO getAllWorkHours(Integer driverId) {
        Driver driver = driverRepository.findById(driverId).orElse(null);
        DriverWorkHoursDTO allHours = new DriverWorkHoursDTO();
        List<WorkHours> workHours = workHoursRepository.findByDriver(driver);
        for (WorkHours w: workHours
             ) {
            WorkHoursDTO whd = new WorkHoursDTO();
            whd.setEnd(w.getEndHours());
            whd.setStart(w.getEndHours());
            whd.setId(w.getId());
            allHours.getResults().add(whd);

        }
        allHours.setTotalCount(workHours.size());
        return allHours;
    }
}
