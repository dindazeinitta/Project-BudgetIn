package com.budgetin.repository;

import com.budgetin.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser_IdOrderByCreatedAtDesc(Long userId);
}

 