package com.example.topgoback.Rides.DTO;

import com.example.topgoback.Rides.DTO.UserRideDTO;
import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class UserRidesListDTO {

    private PaginatedResponse totalCount;

    private List<UserRideDTO> results;

    public UserRidesListDTO(){}

    public UserRidesListDTO(Page<UserRideDTO> page){
        totalCount = new PaginatedResponse();
        totalCount.setTotalCount((int) page.getTotalElements());
        this.results = page.getContent();
    }

    public Integer getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }

    public List<UserRideDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<UserRideDTO> results) {
        this.results = results;
    }

}
