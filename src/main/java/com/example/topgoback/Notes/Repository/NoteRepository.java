package com.example.topgoback.Notes.Repository;

import com.example.topgoback.Notes.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository  extends JpaRepository<Note,Integer> {
}
