package com.example.topgoback.PasswordResetTokens.Controller;

import com.example.topgoback.Documents.DTO.CreateDocumentDTO;
import com.example.topgoback.Documents.DTO.DocumentInfoDTO;
import com.example.topgoback.PasswordResetTokens.Model.PasswordResetToken;
import com.example.topgoback.PasswordResetTokens.Service.PasswordResetTokenService;
import com.example.topgoback.Rides.DTO.UserRidesListDTO;
import com.example.topgoback.Users.DTO.AllDriversDTO;
import com.example.topgoback.Users.DTO.CreateDriverDTO;
import com.example.topgoback.Users.DTO.DriverInfoDTO;
import com.example.topgoback.Users.Service.DriverService;
import com.example.topgoback.Vehicles.DTO.CreateVehicleDTO;
import com.example.topgoback.Vehicles.DTO.VehicleInfoDTO;
import com.example.topgoback.WorkHours.DTO.DriverWorkHoursDTO;
import com.example.topgoback.WorkHours.DTO.WorkHoursDTO;
import com.example.topgoback.WorkHours.Service.WorkHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/passwordResetToken")
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetTokenController {
    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> saveToken(@RequestBody String token) {

        passwordResetTokenService.addOne(token);

        return new ResponseEntity<>("Token saved successfuly", HttpStatus.OK);
    }


    @GetMapping(value = "/{token}")
    public ResponseEntity<PasswordResetToken> findToken(@PathVariable String token) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.findOne(token);

        return new ResponseEntity<>(passwordResetToken, HttpStatus.OK);
    }



    @DeleteMapping(value = "/{token}")
    public ResponseEntity<?> deleteToken(@PathVariable String token) {

        passwordResetTokenService.deleteOne(token);

        return new ResponseEntity<>("Token deleted successfuly", HttpStatus.OK);
    }


}

