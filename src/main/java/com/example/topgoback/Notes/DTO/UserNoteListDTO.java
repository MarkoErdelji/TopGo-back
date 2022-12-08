package com.example.topgoback.Notes.DTO;

import com.example.topgoback.Tools.PaginatedResponse;

import java.util.List;

public class UserNoteListDTO extends PaginatedResponse {

    List<NoteResponseDTO> results;

    public UserNoteListDTO() {
    }

    public List<NoteResponseDTO> getResults() {
        return results;
    }

    public void setResults(List<NoteResponseDTO> results) {
        this.results = results;
    }
}
