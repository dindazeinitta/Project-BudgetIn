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

@Getter
@Setter
@Entity
@Table(name = "users", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // Manually added getters and setters to resolve compilation issues
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
