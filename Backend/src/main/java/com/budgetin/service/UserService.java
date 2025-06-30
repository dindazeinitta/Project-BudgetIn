package com.budgetin.service;

import com.budgetin.model.User;
import com.budgetin.web.dto.RegistrationDto;
import java.util.Optional;

public interface UserService {
    void register(RegistrationDto registrationDto);
    Optional<User> findByEmail(String email);
    void updateProfile(String email, String fullName);
    void changePassword(String email, String currentPassword, String newPassword);
}
