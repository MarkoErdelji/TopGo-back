package com.example.topgoback.WorkHours.Service;

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

    public DriverWorkHoursDTO getAllWorkHours(Integer driverId) {
        DriverWorkHoursDTO allHours = new DriverWorkHoursDTO();
        List<WorkHours> workHours = workHoursRepository.getWorkHoursByDriverId(driverId);
        for (WorkHours w: workHours
             ) {
            WorkHoursDTO whd = new WorkHoursDTO();
            whd.setEnd(w.getEnd());
            whd.setStart(whd.getStart());
            whd.setId(whd.getId());
            allHours.getResults().add(whd);

        }
        allHours.setTotalCount(workHours.size());
        return allHours;
    }
}
