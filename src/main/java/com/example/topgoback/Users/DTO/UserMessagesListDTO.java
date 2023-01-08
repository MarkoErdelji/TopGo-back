package com.example.topgoback.Users.DTO;

import com.example.topgoback.Messages.DTO.UserMessagesDTO;
import com.example.topgoback.Messages.Model.Message;
import com.example.topgoback.Notes.DTO.NoteResponseDTO;
import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class UserMessagesListDTO{

    private PaginatedResponse totalCount;
    private List<UserMessagesDTO> results;


    public UserMessagesListDTO(Page<UserMessagesDTO> page){
        totalCount = new PaginatedResponse();
        totalCount.setTotalCount((int) page.getTotalElements());
        this.results = page.getContent();
    }


    public List<UserMessagesDTO> getResults() {
        return results;
    }

    public void setResults(List<UserMessagesDTO> results) {
        this.results = results;
    }

    public Integer getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }
}
