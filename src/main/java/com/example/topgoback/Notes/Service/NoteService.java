package com.example.topgoback.Notes.Service;

import com.example.topgoback.Notes.DTO.NoteResponseDTO;
import com.example.topgoback.Notes.DTO.UserNoteListDTO;
import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Repository.NoteRepository;
import com.example.topgoback.Users.DTO.CreateNoteDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    public NoteResponseDTO addOne(int userId, CreateNoteDTO note) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!");

        }
        Note newNote = new Note();
        newNote.setUser(user.get());
        newNote.setMessage(note.getMessage());
        newNote.setTimeOfWriting(LocalDateTime.now());

        noteRepository.save(newNote);

        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setMessage(newNote.getMessage());
        noteResponseDTO.setDate(newNote.getTimeOfWriting());
        noteResponseDTO.setId(newNote.getId());
        return noteResponseDTO;
    }

    public UserNoteListDTO findUsersNotes(int userId) {
        UserNoteListDTO userNoteListDTO = new UserNoteListDTO();
        userNoteListDTO.setTotalCount(243);
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setId(10);
        noteResponseDTO.setMessage("The passenger has requested and after that aborted the ride");
        noteResponseDTO.setDate(LocalDateTime.now());
        List<NoteResponseDTO> noteResponseDTOList = new ArrayList<>();
        noteResponseDTOList.add(noteResponseDTO);
        userNoteListDTO.setResults(noteResponseDTOList);
        return userNoteListDTO;

    }
}
