package com.example.topgoback.Panic.Service;

import com.example.topgoback.Panic.DTO.GetAllPanicDTO;
import com.example.topgoback.Panic.DTO.PanicDTO;
import com.example.topgoback.Panic.Model.Panic;
import com.example.topgoback.Panic.Repository.PanicRepository;
import com.example.topgoback.Tools.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PanicService {
    @Autowired
    PanicRepository panicRepository;
    public GetAllPanicDTO getAllPanic(Pageable pageable) {
        GetAllPanicDTO response = new GetAllPanicDTO();
        List<PanicDTO> panicsDTO = new ArrayList<>();

        Page<Panic> panic = panicRepository.findAll(pageable);
        for (Panic p: panic
             ) {
            panicsDTO.add(new PanicDTO(p));


        }
        response.setResults(panicsDTO);
        PaginatedResponse total = new PaginatedResponse();
        total.setTotalCount(panicsDTO.size());
        response.setTotalCount(total);

        return response;



    }
}
