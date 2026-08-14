package com.skyvault.service;

import com.skyvault.dto.JwtAuthResponseDto;
import com.skyvault.dto.LoginRequestDto;
import com.skyvault.dto.RegisterRequestDto;
import com.skyvault.dto.UserResponseDto;
import com.skyvault.exception.SkyVaultApiException;
import com.skyvault.model.Role;
import com.skyvault.model.RoleName;
import com.skyvault.model.User;
import com.skyvault.repository.RoleRepository;
import com.skyvault.repository.UserRepository;
import com.skyvault.security.JwtTokenProvider;
import com.skyvault.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role(1, RoleName.ROLE_ADMIN, "System Administrator");
        sampleUser = new User();
        sampleUser.setId(UUID.randomUUID());
        sampleUser.setUsername("admin_user");
        sampleUser.setEmail("admin@skyvault.aero");
        sampleUser.setPassword("encoded_password");
        sampleUser.setFirstName("Admin");
        sampleUser.setLastName("User");
        sampleUser.setActive(true);
        sampleUser.setRoles(Set.of(adminRole));
    }

    @Test
    @DisplayName("UT-AUTH-01: Successful Login returns valid JWT Access Token")
    void login_Success() {
        LoginRequestDto loginRequest = new LoginRequestDto("admin_user", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("mocked_jwt_token");
        when(userRepository.findByUsernameOrEmail("admin_user", "admin_user")).thenReturn(Optional.of(sampleUser));

        JwtAuthResponseDto response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("admin_user", response.getUsername());
        assertTrue(response.getRoles().contains("ROLE_ADMIN"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
    }

    @Test
    @DisplayName("UT-AUTH-02: Registration fails when username already exists")
    void register_DuplicateUsername_ThrowsException() {
        RegisterRequestDto registerDto = new RegisterRequestDto(
                "admin_user", "new@skyvault.aero", "password123", "New", "User", Set.of("ROLE_ADMIN")
        );

        when(userRepository.existsByUsername("admin_user")).thenReturn(true);

        assertThrows(SkyVaultApiException.class, () -> authService.register(registerDto));
        verify(userRepository, never()).save(any(User.class));
    }
}
