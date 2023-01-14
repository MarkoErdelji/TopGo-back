package com.example.topgoback.Users.DTO;

import com.example.topgoback.Rides.Model.Ride;
import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.Model.User;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class UserListDTO {

    private PaginatedResponse totalCount;

    private List<UserListResponseDTO> results;

    public UserListDTO(Page<UserListResponseDTO> page){
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

    public List<UserListResponseDTO> getResults() {
        return results;
    }

    public void setResults(ArrayList<UserListResponseDTO> results) {
        this.results = results;
    }
}
