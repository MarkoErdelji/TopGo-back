package com.example.topgoback.ProfileChangesRequest.Controller;

import com.example.topgoback.ProfileChangesRequest.Service.ProfileChangesRequestService;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "api/profileChangesRequest")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileChangesRequestController {

    @Autowired
    ProfileChangesRequestService profileChangesRequestService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> saveProfileChangesRequest(@RequestBody DriverInfoDTO profileChangesInfo) {

        try{
        profileChangesRequestService.addOne(profileChangesInfo);

        return new ResponseEntity<>("Request successfuly created", HttpStatus.CREATED);}
        catch(Exception e){
            return new ResponseEntity<>("Error: Request could not be created",HttpStatus.BAD_REQUEST);
        }
    }
}
