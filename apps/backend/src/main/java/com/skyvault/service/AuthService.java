package com.skyvault.service;

import com.skyvault.dto.JwtAuthResponseDto;
import com.skyvault.dto.LoginRequestDto;
import com.skyvault.dto.RegisterRequestDto;
import com.skyvault.dto.UserResponseDto;

public interface AuthService {
    JwtAuthResponseDto login(LoginRequestDto loginRequestDto);
    UserResponseDto register(RegisterRequestDto registerRequestDto);
}
