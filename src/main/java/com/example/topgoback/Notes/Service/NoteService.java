package com.example.topgoback.Notes.Service;

import com.example.topgoback.Notes.DTOS.NoteResponseDTO;
import com.example.topgoback.Notes.DTOS.UserNoteListDTO;
import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    NoteRepository noteRepository;


    public NoteResponseDTO addOne(Note note) {
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setMessage(note.getMessage());
        noteResponseDTO.setId(10);
        noteResponseDTO.setTimeOfPosting(LocalDateTime.now());
        return noteResponseDTO;
       // noteRepository.save(note);
    }

    public UserNoteListDTO findUsersNotes(int userId) {
        UserNoteListDTO userNoteListDTO = new UserNoteListDTO();
        userNoteListDTO.setTotalCount(243);
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setId(10);
        noteResponseDTO.setMessage("The passenger has requested and after that aborted the ride");
        noteResponseDTO.setTimeOfPosting(LocalDateTime.now());
        List<NoteResponseDTO> noteResponseDTOList = new ArrayList<>();
        noteResponseDTOList.add(noteResponseDTO);
        userNoteListDTO.setResults(noteResponseDTOList);
        return userNoteListDTO;

    }
}
