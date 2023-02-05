package com.example.topgoback.Notes.Repository;

import com.example.topgoback.Notes.Model.Note;
import com.example.topgoback.Users.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository  extends JpaRepository<Note,Integer> {
    Page<Note> findByUser(User u, Pageable pageable);
}
