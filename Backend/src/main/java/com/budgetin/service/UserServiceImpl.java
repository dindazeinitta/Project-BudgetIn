package com.budgetin.service;

import com.budgetin.model.User;
import com.budgetin.repository.UserRepository;
import com.budgetin.web.dto.RegistrationDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void register(RegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.email())) {
            throw new IllegalStateException("Email already in use");
        }
        User user = new User();
        user.setUsername(registrationDto.username());
        user.setFullName(registrationDto.fullName());
        user.setEmail(registrationDto.email());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void updateProfile(String email, String fullName, String newEmail) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setFullName(fullName);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateProfile(String email, String fullName, String newEmail, MultipartFile profilePicture) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Check if the new email is already taken by another user
        if (!email.equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new IllegalStateException("Email " + newEmail + " is already in use.");
        }

        user.setFullName(fullName);
        user.setEmail(newEmail);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String pictureUrl = saveProfilePicture(profilePicture);
                user.setProfilePicture(pictureUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save profile picture", e);
            }
        }

        return userRepository.save(user);
    }

    private String saveProfilePicture(MultipartFile file) throws IOException {
        String uploadDir = "uploads/profile-pictures";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return "/" + uploadDir + "/" + fileName;
    }

    @Override
    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalStateException("Wrong current password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void createPasswordResetTokenForUser(User user, String token) {
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
    }

    @Override
    public Optional<User> validatePasswordResetToken(String token) {
        return userRepository.findByResetToken(token)
                .filter(user -> user.getResetTokenExpiry().isAfter(LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }
}
