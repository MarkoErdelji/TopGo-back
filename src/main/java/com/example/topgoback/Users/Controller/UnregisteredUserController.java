package com.example.topgoback.Users.Controller;

import com.example.topgoback.Users.DTO.UnregisteredUserAssumptionDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserDTO;
import com.example.topgoback.Users.Service.UnregisteredUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/unregisteredUser/")
@CrossOrigin(origins = "http://localhost:4200")
public class UnregisteredUserController {

    @Autowired
    UnregisteredUserService unregisteredUserService;
    @PostMapping(consumes = "application/json")
    public ResponseEntity<UnregisteredUserAssumptionDTO> getAssumption(@RequestBody UnregisteredUserDTO unregisteredUserDTO) {

        UnregisteredUserAssumptionDTO unregisteredUserAssumptionDTO = unregisteredUserService.getAssumptionFromData(unregisteredUserDTO);

        return new ResponseEntity<>(unregisteredUserAssumptionDTO, HttpStatus.OK);
    }

}
