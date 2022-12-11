package com.example.topgoback.Panic.Service;

import com.example.topgoback.Panic.DTO.GetAllPanicDTO;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Users.DTO.UserListDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PanicService {
    public GetAllPanicDTO getAllPanic() {
        GetAllPanicDTO response = new GetAllPanicDTO();
        response.setTotalCount(243);
        PanicDTO panic = new PanicDTO();
        panic.setId(10);
        panic.setRide(UserRideDTO.getMockupData());
        panic.setUser(UserListResponseDTO.getMockupData());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        panic.setTime(LocalDateTime.parse("2022-12-11T13:34:42Z",formatter));
        panic.setReason("Driver is drinking while driving");
        List<PanicDTO> list = new ArrayList<>();
        list.add(panic);
        response.setResults(list);
        return response;



    }
}
