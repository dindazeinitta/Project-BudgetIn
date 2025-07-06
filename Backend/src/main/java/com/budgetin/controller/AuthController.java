package com.budgetin.controller;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

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
import com.budgetin.service.CaptchaService;
import com.budgetin.service.EmailService;
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
    private final CaptchaService captchaService;
    private final EmailService emailService;

    private final boolean cookieSecure;

    public AuthController(
        UserService userService, 
        AuthenticationManager authenticationManager, 
        JwtTokenProvider tokenProvider,
        CaptchaService captchaService,
        EmailService emailService,
        @Value("${app.cookie.secure:false}") boolean cookieSecure
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.captchaService = captchaService;
        this.emailService = emailService;
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
        // Verify CAPTCHA first
        if (!captchaService.verifyCaptcha(loginRequest.captchaToken())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(false, "CAPTCHA verification failed"));
        }

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        return userService.findByEmail(email)
                .map(user -> {
                    String token = UUID.randomUUID().toString();
                    userService.createPasswordResetTokenForUser(user, token);
                    emailService.sendEmail(user.getEmail(), "Reset Password",
                            "To reset your password, click the link below:\n"
                                    + "http://127.0.0.1:5501/Frontend/signin.html?token=" + token);
                    return ResponseEntity.ok(new ApiResponse(true, "Password reset link sent to email"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Email not found")));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String password = body.get("password");

        return userService.validatePasswordResetToken(token)
                .map(user -> {
                    userService.changeUserPassword(user, password);
                    return ResponseEntity.ok(new ApiResponse(true, "Password successfully changed"));
                })
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Invalid or expired token")));
    }
}
