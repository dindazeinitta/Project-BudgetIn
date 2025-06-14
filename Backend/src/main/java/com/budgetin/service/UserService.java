package com.budgetin.service;

import com.budgetin.model.User;
import com.budgetin.repository.UserRepository;
import com.budgetin.web.dto.RegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Dependency Injection via constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegistrationDto registrationDto) {
        // 1. Cek email sudah terdaftar
        if (userRepository.existsByEmail(registrationDto.email())) {
            throw new IllegalStateException("Email already registered");
        }

        // 2. Buat user baru
        User user = new User();
        user.setFullName(registrationDto.fullName());
        user.setEmail(registrationDto.email());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));

        // 3. Simpan ke database
        return userRepository.save(user);
    }
}
