package com.example.topgoback.ProfileChangesRequest.Service;

import com.example.topgoback.ProfileChangesRequest.DTO.AllProfileChangesRequestsDTO;
import com.example.topgoback.ProfileChangesRequest.DTO.ProfileChangeRequestDTO;
import com.example.topgoback.ProfileChangesRequest.Model.ProfileChangesRequest;
import com.example.topgoback.ProfileChangesRequest.Repository.ProfileChangesRequestRepository;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Model.Driver;
import com.example.topgoback.Users.Repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public AllProfileChangesRequestsDTO getAll(){
        AllProfileChangesRequestsDTO allProfileChangesRequestsDTO = new AllProfileChangesRequestsDTO();


        List<ProfileChangeRequestDTO> pcrDTOS = new ArrayList<ProfileChangeRequestDTO>();
        for(ProfileChangesRequest prc: this.profileChangesRequestRepository.findAll()){
            ProfileChangeRequestDTO pcrDTO = new ProfileChangeRequestDTO();
            pcrDTO.setId(prc.getId());
            pcrDTO.setAddress(prc.getAddress());
            pcrDTO.setDriverId(prc.getDriver().getId());
            pcrDTO.setEmail(prc.getEmail());
            pcrDTO.setFirstName(prc.getFirstName());
            pcrDTO.setLastName(prc.getLastName());
            pcrDTO.setPhoneNumber(prc.getPhoneNumber());
            pcrDTO.setProfilePicture(prc.getProfilePicture());
            pcrDTOS.add(pcrDTO);
        }
        allProfileChangesRequestsDTO.setProfileChangeRequestDTOS(pcrDTOS);
        allProfileChangesRequestsDTO.setCount(pcrDTOS.size());
        return allProfileChangesRequestsDTO;
    }
}
