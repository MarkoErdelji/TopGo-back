package com.example.topgoback.Notes.Service;

import com.example.topgoback.Notes.DTO.NoteResponseDTO;
import com.example.topgoback.Notes.DTO.UserNoteListDTO;
import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Repository.NoteRepository;
import com.example.topgoback.Users.DTO.CreateNoteDTO;
import com.example.topgoback.Users.DTO.UserListDTO;
import com.example.topgoback.Users.DTO.UserListResponseDTO;
import com.example.topgoback.Users.Model.User;
import com.example.topgoback.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public UserNoteListDTO findUsersNotes(int userId, Pageable pageable) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist!");

        }
        Page<Note> page = noteRepository.findAll(pageable);
        List<NoteResponseDTO> noteResponseDTOList = NoteResponseDTO.convertToNoteResponseDTO(page.getContent());

        UserNoteListDTO notes = new UserNoteListDTO(new PageImpl<>(noteResponseDTOList, pageable, page.getTotalElements()));
        return notes;

    }
}
