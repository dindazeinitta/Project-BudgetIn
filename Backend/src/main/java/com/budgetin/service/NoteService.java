package com.budgetin.service;

import com.budgetin.model.Note;
import com.budgetin.repository.NoteRepository;
import com.budgetin.repository.UserRepository;
import com.budgetin.web.dto.NoteRequest;
import com.budgetin.web.dto.NoteResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository; // Untuk mengambil user yang sedang login

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    // Helper untuk mendapatkan user yang sedang login
    private com.budgetin.model.User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated.");
        }
        String email = authentication.getName(); // Mengambil email dari principal
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database."));
    }

    // Membuat Note baru
    @Transactional
    public NoteResponse createNote(NoteRequest noteRequest) {
        com.budgetin.model.User currentUser = getCurrentAuthenticatedUser(); // Dapatkan user yang sedang login
        Note note = new Note();
        note.setUser(currentUser);
        note.setContent(noteRequest.getContent());
        Note savedNote = noteRepository.save(note);
        return new NoteResponse(savedNote.getId(), savedNote.getContent(), savedNote.getCreatedAt(), savedNote.getUser().getId());
    }

    // Mendapatkan semua Note untuk user yang sedang login
    public List<NoteResponse> getNotesForCurrentUser() {
        com.budgetin.model.User currentUser = getCurrentAuthenticatedUser();
        List<Note> notes = noteRepository.findByUser_IdOrderByCreatedAtDesc(currentUser.getId());
        return notes.stream()
                .map(note -> new NoteResponse(note.getId(), note.getContent(), note.getCreatedAt(), note.getUser().getId()))
                .collect(Collectors.toList());
    }

    // Mengupdate Note berdasarkan ID (hanya jika note tersebut milik user yang login)
    @Transactional
    public NoteResponse updateNote(Long noteId, NoteRequest noteRequest) {
        com.budgetin.model.User currentUser = getCurrentAuthenticatedUser();
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with ID: " + noteId));

        if (!existingNote.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("User not authorized to update this note.");
        }

        existingNote.setContent(noteRequest.getContent());
        Note updatedNote = noteRepository.save(existingNote);
        return new NoteResponse(updatedNote.getId(), updatedNote.getContent(), updatedNote.getCreatedAt(), updatedNote.getUser().getId());
    }

    // Menghapus Note berdasarkan ID (hanya jika note tersebut milik user yang login)
    @Transactional
    public void deleteNote(Long noteId) {
        com.budgetin.model.User currentUser = getCurrentAuthenticatedUser();
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with ID: " + noteId));

        if (!existingNote.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("User not authorized to delete this note.");
        }

        noteRepository.delete(existingNote);
    }

    public Optional<Note> getNotes(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNotes'");
    }
}
