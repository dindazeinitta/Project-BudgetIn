package com.budgetin.controller;

import com.budgetin.config.JwtUtil;
import com.budgetin.model.User;
import com.budgetin.service.UserService;
import com.budgetin.web.dto.ApiResponse;
import com.budgetin.web.dto.ChangePasswordDto;
import com.budgetin.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final boolean cookieSecure;

    public UserController(UserService userService, JwtUtil jwtUtil, @Value("${app.cookie.secure:false}") boolean cookieSecure) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.cookieSecure = cookieSecure;
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam("fullName") String fullName,
                                           @RequestParam("email") String email,
                                           @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                           Principal principal) {
        String oldEmail = principal.getName();
        User updatedUser = userService.updateProfile(oldEmail, fullName, email, profilePicture);

        String newJwt = jwtUtil.generateToken(updatedUser);

        ResponseCookie cookie = ResponseCookie.from("jwt", newJwt)
            .httpOnly(true)
            .secure(cookieSecure)
            .path("/")
            .maxAge(60 * 60 * 24) // 1 day
            .build();

        UserResponse response = new UserResponse(true, "Profile updated successfully", updatedUser.getFullName(), updatedUser.getEmail(), updatedUser.getUsername(), updatedUser.getProfilePicture());

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(response);
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto passwordDto, Principal principal) {
        // IllegalStateException for wrong password will be caught by RestExceptionHandler
        userService.changePassword(principal.getName(), passwordDto.currentPassword(), passwordDto.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
    }
}
