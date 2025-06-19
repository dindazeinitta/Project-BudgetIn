package com.budgetin.repository;

import com.budgetin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Cari user by email
    boolean existsByEmail(String email);      // Cek email sudah terdaftar
}
