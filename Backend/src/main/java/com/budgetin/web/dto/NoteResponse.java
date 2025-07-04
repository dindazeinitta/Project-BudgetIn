package com.budgetin.web.dto;

import java.time.LocalDateTime;

public class NoteResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId; // ID user yang memiliki note ini

    public NoteResponse(Long id, String content, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    // Setters (jika dibutuhkan, tapi untuk response biasanya hanya getter)
    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}