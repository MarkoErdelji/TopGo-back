package com.example.topgoback.Users.Controller;

import com.example.topgoback.Users.DTO.UnregisteredUserAssumptionDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/unregisteredUser/")
@CrossOrigin(origins = "http://localhost:4200")
public class UnregisteredUserController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<UnregisteredUserAssumptionDTO> getAssumption(@RequestBody UnregisteredUserDTO unregisteredUserDTO) {
        UnregisteredUserAssumptionDTO unregisteredUserAssumptionDTO = new UnregisteredUserAssumptionDTO();
        unregisteredUserAssumptionDTO.setEstimatedCost(450);
        unregisteredUserAssumptionDTO.setEstimatedTimeInMinutes(10);
        return new ResponseEntity<>(unregisteredUserAssumptionDTO, HttpStatus.OK);
    }

}
