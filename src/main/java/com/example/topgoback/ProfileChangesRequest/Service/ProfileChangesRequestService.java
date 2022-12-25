package com.example.topgoback.ProfileChangesRequest.Service;

import com.example.topgoback.ProfileChangesRequest.Model.ProfileChangesRequest;
import com.example.topgoback.ProfileChangesRequest.Repository.ProfileChangesRequestRepository;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileChangesRequestService {

    @Autowired
    private ProfileChangesRequestRepository profileChangesRequestRepository;

    @Autowired
    private DriverRepository driverRepository;

    public void addOne(DriverInfoDTO profileChangesRequestCreateDTO) throws Exception {
        ProfileChangesRequest profileChangesRequest = new ProfileChangesRequest();
        profileChangesRequest.setProfilePicture(profileChangesRequestCreateDTO.getProfilePicture());
        profileChangesRequest.setFirstName(profileChangesRequestCreateDTO.getName());
        profileChangesRequest.setLastName(profileChangesRequestCreateDTO.getSurname());
        profileChangesRequest.setAddress(profileChangesRequestCreateDTO.getAddress());
        profileChangesRequest.setEmail(profileChangesRequestCreateDTO.getEmail());
        profileChangesRequest.setPhoneNumber(profileChangesRequestCreateDTO.getTelephoneNumber());
        Optional<Driver> driver = driverRepository.findById(profileChangesRequestCreateDTO.getId());
        if(driver.isPresent()){
            profileChangesRequest.setDriver(driver.get());
        }
        else{
            throw new Exception("Driver is not present");
        }
        profileChangesRequestRepository.save(profileChangesRequest);
    }
}
