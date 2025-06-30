package com.budgetin.controller;

import com.budgetin.service.UserService;
import com.budgetin.web.dto.ApiResponse;
import com.budgetin.web.dto.ChangePasswordDto;
import com.budgetin.web.dto.UpdateProfileDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileDto profileDto, Principal principal) {
        userService.updateProfile(principal.getName(), profileDto.fullName());
        return ResponseEntity.ok(new ApiResponse(true, "Profile updated successfully"));
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto passwordDto, Principal principal) {
        // IllegalStateException for wrong password will be caught by RestExceptionHandler
        userService.changePassword(principal.getName(), passwordDto.currentPassword(), passwordDto.newPassword());
        return ResponseEntity.ok(new ApiResponse(true, "Password changed successfully"));
    }
}
