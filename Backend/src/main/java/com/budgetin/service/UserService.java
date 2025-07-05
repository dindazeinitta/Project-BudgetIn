package com.budgetin.service;

import com.budgetin.model.User;
import com.budgetin.web.dto.RegistrationDto;
import java.util.Optional;

public interface UserService {
    void register(RegistrationDto registrationDto);
    Optional<User> findByEmail(String email);
    void updateProfile(String email, String fullName, String newEmail);
    User updateProfile(String name, String fullName, String email, org.springframework.web.multipart.MultipartFile profilePicture);
    void changePassword(String email, String currentPassword, String newPassword);
    void createPasswordResetTokenForUser(User user, String token);
    Optional<User> validatePasswordResetToken(String token);
    void changeUserPassword(User user, String password);
}
