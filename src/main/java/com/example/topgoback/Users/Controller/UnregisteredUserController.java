package com.example.topgoback.Users.Controller;

import com.example.topgoback.Users.DTO.UnregisteredUserAssumptionDTO;
import com.example.topgoback.Users.DTO.UnregisteredUserDTO;
import com.example.topgoback.Users.Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/unregisteredUser/")
public class UnregisteredUserController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<UnregisteredUserAssumptionDTO> getAssumption(@RequestBody UnregisteredUserDTO unregisteredUserDTO) {
        UnregisteredUserAssumptionDTO unregisteredUserAssumptionDTO = new UnregisteredUserAssumptionDTO();
        unregisteredUserAssumptionDTO.setAssumedPrice(1200);
        unregisteredUserAssumptionDTO.setAssumedTimeInMinutes(15);
        return new ResponseEntity<>(unregisteredUserAssumptionDTO, HttpStatus.CREATED);
    }

}
