package com.example.topgoback.Panic.Controller;

import com.example.topgoback.Panic.DTO.GetAllPanicDTO;
import com.example.topgoback.Panic.Service.PanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/panic")
public class PanicController {
    @Autowired
    private PanicService panicService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN') || hasAnyRole('USER')")
    public ResponseEntity<GetAllPanicDTO> getPanic(@RequestParam(required = false,defaultValue = "0") Integer page,
                                                   @RequestParam(required = false,defaultValue = "10") Integer size,
                                                   Pageable pageable)
    {

        pageable = (Pageable) PageRequest.of(page, size, Sort.by("id").ascending());
        GetAllPanicDTO response = panicService.getAllPanic(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
