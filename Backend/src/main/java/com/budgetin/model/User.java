package com.budgetin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"email"}),
           @UniqueConstraint(columnNames = {"username"})
       })
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(max = 50, message = "Username harus kurang dari 50 karakter")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Nama Tidak Boleh Kosong")
    @Size(max = 100, message = "Nama Harus Kurang dari 100 Karakter")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format Email Tidak Benar")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password Tidak Boleh Kosong")
    @Size(min = 6, message = "Password harus lebih dari 6 karakter")
    @Column(nullable = false)
    private String password;

    @Column
    private String profilePicture;

    @Column
    private String resetToken;

    @Column
    private LocalDateTime resetTokenExpiry;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Long getId() {
        return id;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }
}
