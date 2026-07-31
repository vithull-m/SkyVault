package com.skyvault.service.impl;

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
import com.skyvault.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public JwtAuthResponseDto login(LoginRequestDto loginRequestDto) {
        // Authenticate credentials against SecurityContext
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsernameOrEmail(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = tokenProvider.generateToken(authentication);

        // Fetch User details for response payload
        User user = userRepository.findByUsernameOrEmail(
                loginRequestDto.getUsernameOrEmail(),
                loginRequestDto.getUsernameOrEmail()
        ).orElseThrow(() -> new SkyVaultApiException(HttpStatus.NOT_FOUND, "User not found"));

        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new JwtAuthResponseDto(token, user.getId(), user.getUsername(), user.getEmail(), roles);
    }

    @Override
    public UserResponseDto register(RegisterRequestDto registerRequestDto) {
        // Check for duplicate username
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new SkyVaultApiException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }

        // Check for duplicate email
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new SkyVaultApiException(HttpStatus.BAD_REQUEST, "Email is already registered!");
        }

        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setActive(true);

        Set<Role> roles = new HashSet<>();
        for (String roleStr : registerRequestDto.getRoles()) {
            RoleName roleName;
            try {
                roleName = RoleName.valueOf(roleStr);
            } catch (IllegalArgumentException e) {
                throw new SkyVaultApiException(HttpStatus.BAD_REQUEST, "Invalid role specified: " + roleStr);
            }

            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        // Create role dynamically if not seeded yet
                        Role newRole = new Role();
                        newRole.setName(roleName);
                        newRole.setDescription("System generated role for " + roleName.name());
                        return roleRepository.save(newRole);
                    });
            roles.add(role);
        }

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        Set<String> roleNames = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.isActive(),
                roleNames,
                savedUser.getCreatedAt()
        );
    }
}
