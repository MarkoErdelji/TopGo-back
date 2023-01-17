package com.example.topgoback.WorkHours.Service;

import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Repository.DriverRepository;
import com.example.topgoback.Users.Service.DriverService;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import com.example.topgoback.WorkHours.Model.WorkHours;
import com.example.topgoback.WorkHours.Repository.WorkHoursRepository;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public boolean checkIfDriverExceeded8Hours(List<WorkHours> workHours){
        long fullWorkTime = 0;
        for (WorkHours workHour: workHours){
            fullWorkTime += workHour.getDifferenceInSeconds();
        }
        long hoursInSeconds = 8 * 60 * 60;
        return fullWorkTime > hoursInSeconds;
    }

    public boolean isShiftAlreadyOngoing(List<WorkHours> workHours){
        for (WorkHours workHour:workHours){
            if(workHour.getEndHours() == null){
                return true;
            }
        }
        return false;
    }
    public WorkHours addOne(LocalDateTime start, int driverId){
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }
        if(driver.get().getVehicle() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot start shift because the vehicle is not defined!");
        }
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.toLocalDate());
        LocalDateTime startInterval = now.minusDays(1);
        List<WorkHours> workHoursWithinLast24Hours = workHoursRepository.findWorkHoursWithinLast24Hours(startInterval,
                now, driver.get());

        if(isShiftAlreadyOngoing(workHoursWithinLast24Hours)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Shift already ongoing!");

        }
        if(checkIfDriverExceeded8Hours(workHoursWithinLast24Hours)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot start shift because you exceeded the 8 hours limit in last 24 hours!");
        }
        WorkHours workHours = new WorkHours();
        workHours.setStartHours(start);
        workHours.setEndHours(null);
        workHours.setDriver(driver.get());
        workHoursRepository.save(workHours);
        return workHours;

    }
    public WorkHours findById(int id){
        Optional<WorkHours> workHours = workHoursRepository.findById(id);
        if(workHours.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Working hour does not exist!");
        }
        return workHours.get();
    }
    public WorkHours updateOne(int id, LocalDateTime end){
        WorkHours workHours = findById(id);
        Driver driver = workHours.getDriver();
        List<WorkHours> driverWorkHours = workHoursRepository.findByDriver(driver);
        if(driver.getVehicle() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot end shift because the vehicle is not defined!");
        }
        if(!isShiftAlreadyOngoing(driverWorkHours)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No shift is ongoing!");
        }
        workHours.setEndHours(end);
        workHoursRepository.save(workHours);
        return workHours;
    }

    public DriverWorkHoursDTO findWorkingHoursByDriversId(int driverId, Pageable pageable, LocalDateTime beginDateTimeInterval, LocalDateTime endDateTimeInterval){
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver does not exist!");
        }

        Page<WorkHours> workHoursPage = workHoursRepository.findByDriverAndBeginBetween(driver.get().getId(), beginDateTimeInterval, endDateTimeInterval, pageable);
        List<WorkHoursDTO> workHoursDTOS = WorkHoursDTO.convertToWorkHourDTO(workHoursPage.getContent());
        DriverWorkHoursDTO driverWorkHoursDTO = new DriverWorkHoursDTO(new PageImpl<>(workHoursDTOS, pageable, workHoursPage.getTotalElements()));
        driverWorkHoursDTO.setTotalCount((int) workHoursPage.getTotalElements());
        return driverWorkHoursDTO;
    }
}
