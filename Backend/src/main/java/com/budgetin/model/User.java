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
import lombok.Data;

@Data
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
}
