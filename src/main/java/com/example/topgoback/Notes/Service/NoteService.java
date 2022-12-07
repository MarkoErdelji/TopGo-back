package com.example.topgoback.Notes.Service;

import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Notes.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    NoteRepository noteRepository;


    public void addOne(Note note) {
        noteRepository.save(note);
    }
}
