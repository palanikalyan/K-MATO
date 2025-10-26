package com.foodordering.service;

import com.foodordering.dto.AuthResponseDto;
import com.foodordering.dto.LoginDto;
import com.foodordering.dto.UserDto;
import com.foodordering.dto.UserRegistrationDto;
import com.foodordering.entity.User;
import com.foodordering.enums.Role;
import com.foodordering.exception.ResourceAlreadyExistsException;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.UserRepository;
import com.foodordering.security.JwtTokenProvider;
import com.foodordering.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider tokenProvider;
        private final UserMapper userMapper;

        public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                          AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserMapper userMapper) {
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.authenticationManager = authenticationManager;
                this.tokenProvider = tokenProvider;
                this.userMapper = userMapper;
        }

    @Transactional
    public AuthResponseDto register(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }

        // Map registration DTO to entity and handle password encoding
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setIsActive(true);

        user = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        String token = tokenProvider.generateToken(authentication);

        AuthResponseDto response = userMapper.toAuthResponse(user);
        response.setToken(token);
        return response;
    }

    public AuthResponseDto login(LoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AuthResponseDto response = userMapper.toAuthResponse(user);
        response.setToken(token);
        return response;
    }

    public UserDto getCurrentUser() {
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }
}
