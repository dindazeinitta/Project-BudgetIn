package com.budgetin.controller;

import com.budgetin.service.NoteService;
import com.budgetin.web.dto.NoteRequest;
import com.budgetin.web.dto.NoteResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes") // Base path untuk semua endpoint notes
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // Endpoint untuk membuat note baru
    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@Valid @RequestBody NoteRequest noteRequest) {
        NoteResponse newNote = noteService.createNote(noteRequest);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }

    // Endpoint untuk mendapatkan semua notes untuk user yang sedang login
    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAllNotesForCurrentUser() {
        List<NoteResponse> notes = noteService.getNotesForCurrentUser();
        return ResponseEntity.ok(notes);
    }

    // Endpoint untuk mengupdate note
    @PutMapping("/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long id, @Valid @RequestBody NoteRequest noteRequest) {
        try {
            NoteResponse updatedNote = noteService.updateNote(id, noteRequest);
            return ResponseEntity.ok(updatedNote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Note tidak ditemukan
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Tidak berhak mengupdate
        }
    }

    // Endpoint untuk menghapus note
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        try {
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build(); // Respon 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Note tidak ditemukan
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Tidak berhak menghapus
        }
    }
}
