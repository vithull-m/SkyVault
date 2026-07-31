package com.skyvault.controller;

import com.skyvault.dto.*;
import com.skyvault.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User Login Endpoint (Public)
     * POST /api/v1/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<JwtAuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtAuthResponseDto authResponse = authService.login(loginRequestDto);
        ApiResponseDto<JwtAuthResponseDto> apiResponse = new ApiResponseDto<>(
                true,
                "Authentication successful",
                authResponse
        );
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * User Registration Endpoint (Admin Only)
     * POST /api/v1/auth/register
     */
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserResponseDto userResponse = authService.register(registerRequestDto);
        ApiResponseDto<UserResponseDto> apiResponse = new ApiResponseDto<>(
                true,
                "User registered successfully",
                userResponse
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
