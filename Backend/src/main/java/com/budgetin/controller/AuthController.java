package com.budgetin.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budgetin.config.JwtTokenProvider;
import com.budgetin.service.UserService;
import com.budgetin.web.dto.ApiResponse;
import com.budgetin.web.dto.LoginRequest;
import com.budgetin.web.dto.LoginResponse;
import com.budgetin.web.dto.RegistrationDto;
import com.budgetin.web.dto.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    private final boolean cookieSecure;

    public AuthController(
        UserService userService, 
        AuthenticationManager authenticationManager, 
        JwtTokenProvider tokenProvider,
        @Value("${app.cookie.secure:false}") boolean cookieSecure
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.cookieSecure = cookieSecure;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto registrationDto) {
        // @Valid akan memicu RestExceptionHandler jika validasi gagal.
        // userService akan melempar IllegalStateException jika email sudah ada, yang juga ditangani oleh handler.
        userService.register(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse(true, "Registration successful")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.email(), 
                loginRequest.password()
            )
        );

        String jwt = tokenProvider.generateToken(authentication);

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
            .httpOnly(true)
            .secure(cookieSecure)
            .path("/")
            .maxAge(60 * 60 * 24) // 1 day
            .sameSite("Lax") // Explicitly set SameSite
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new LoginResponse(true, "Login successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "No user logged in"));
        }
        
        // Gunakan gaya fungsional dan biarkan RestExceptionHandler yang menangani error.
        // Ini lebih bersih dan konsisten.
        return userService.findByEmail(principal.getName())
                .map(user -> ResponseEntity.ok(new UserResponse(true, "User found", user.getFullName(), user.getEmail(), user.getUsername(), user.getProfilePicture())))
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(cookieSecure)
            .path("/")
            .maxAge(0)
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new ApiResponse(true, "Logout successful"));
    }
}
