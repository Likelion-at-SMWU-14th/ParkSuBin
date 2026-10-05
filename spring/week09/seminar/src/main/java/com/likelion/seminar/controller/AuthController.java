package com.likelion.seminar.controller;

import com.likelion.seminar.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public AuthService.TokenResponse login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(
                request.username(),
                request.password()
        );
    }

    @PostMapping("/auth/refresh")
    public AuthService.TokenResponse refresh(
            @RequestBody RefreshRequest request
    ) {
        return authService.refresh(request.refreshToken());
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "message", "인증된 사용자입니다."
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleException(
            ResponseStatusException e
    ) {
        String message = e.getReason() == null
                ? "요청을 처리할 수 없습니다."
                : e.getReason();

        return ResponseEntity.status(e.getStatusCode())
                .body(Map.of("message", message));
    }

    public record LoginRequest(
            String username,
            String password
    ) {
    }

    public record RefreshRequest(String refreshToken) {
    }
}