package com.example.topgoback.Notes.DTO;

import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.Model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteResponseDTO {
    private Integer id;

    private String message;

    private LocalDateTime date;

    public static List<NoteResponseDTO> convertToNoteResponseDTO(List<Note> notes) {
        List<NoteResponseDTO> noteResponseDTOList = new ArrayList<NoteResponseDTO>();
        for(Note n:notes){
            noteResponseDTOList.add(new NoteResponseDTO(n));
        }
        return noteResponseDTOList;
    }

    public NoteResponseDTO(){}
    NoteResponseDTO(Note n ){
        this.id = n.getId();
        this.date = n.getTimeOfWriting();
        this.message = n.getMessage();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
