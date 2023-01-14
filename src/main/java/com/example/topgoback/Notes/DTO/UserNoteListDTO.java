package com.example.topgoback.Notes.DTO;

import com.example.topgoback.Tools.PaginatedResponse;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public class UserNoteListDTO {

    PaginatedResponse totalCount;
    List<NoteResponseDTO> results;

    public UserNoteListDTO(Page<NoteResponseDTO> page) {
        totalCount = new PaginatedResponse();
        totalCount.setTotalCount((int) page.getTotalElements());
        this.results = page.getContent();
    }

    public List<NoteResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<NoteResponseDTO> results) {
        this.results = results;
    }

    public Integer getTotalCount() {
        return totalCount.getTotalCount();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setTotalCount(totalCount);
    }
}
