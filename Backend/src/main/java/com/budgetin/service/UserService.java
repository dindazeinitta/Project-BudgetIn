package com.budgetin.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.budgetin.model.User;
import com.budgetin.web.dto.RegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final String USER_DATA_FILE = "src/main/java/com/budgetin/data/user.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Dependency Injection via constructor
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegistrationDto registrationDto) throws IOException {
        // 1. Read existing users from JSON file
        UserData userData = readUserData();
        
        // 2. Check if email already registered
        if (userData.getUsers().stream().anyMatch(user -> user.getEmail().equals(registrationDto.email()))) {
            throw new IllegalStateException("Email already registered");
        }

        // 3. Create new user
        User user = new User();
        user.setId(generateNextId(userData.getUsers()));
        user.setFullName(registrationDto.fullName());
        user.setEmail(registrationDto.email());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));

        // 4. Add user to list
        userData.getUsers().add(user);
        
        // 5. Save updated list to JSON file
        writeUserData(userData);
        
        return user;
    }
    
    public Optional<User> findByEmail(String email) throws IOException {
        UserData userData = readUserData();
        return userData.getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
    
    public boolean validateUser(String email, String password) throws IOException {
        Optional<User> userOptional = findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
    
    private UserData readUserData() throws IOException {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return new UserData(new ArrayList<>());
        }
        return objectMapper.readValue(file, UserData.class);
    }
    
    private void writeUserData(UserData userData) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(USER_DATA_FILE), userData);
    }
    
    private Long generateNextId(List<User> users) {
        return users.isEmpty() ? 1L : users.stream()
                .mapToLong(User::getId)
                .max()
                .getAsLong() + 1;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> userOptional = findByEmail(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return org.springframework.security.core.userdetails.User
                        .builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles("USER")
                        .build();
            }
        } catch (IOException e) {
            throw new UsernameNotFoundException("Error reading user data", e);
        }
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
    
    // Helper class to map JSON structure
    private static class UserData {
        private List<User> users;
        
        public UserData() {
            this.users = new ArrayList<>();
        }
        
        public UserData(List<User> users) {
            this.users = users;
        }
        
        public List<User> getUsers() {
            return users;
        }
        
        public void setUsers(List<User> users) {
            this.users = users;
        }
    }
}
