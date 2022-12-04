package com.example.topgoback.Users.Service;

import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public List<Driver> findAll() { return driverRepository.findAll();}

    public void addOne(Driver user) { driverRepository.save(user);}
}
