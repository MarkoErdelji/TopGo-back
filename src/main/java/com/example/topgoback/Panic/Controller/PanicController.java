package com.example.topgoback.Panic.Controller;

import com.example.topgoback.Panic.DTO.GetAllPanicDTO;
import com.example.topgoback.Panic.Service.PanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/panic")
public class PanicController {
    @Autowired
    private PanicService panicService;

    @GetMapping
    public ResponseEntity<GetAllPanicDTO> getPanic()
    {
        GetAllPanicDTO response = panicService.getAllPanic();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
