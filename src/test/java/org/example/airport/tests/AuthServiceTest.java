package org.example.airport.tests;

import org.example.airport.dto.RegisterRequest;
import org.example.airport.entity.User;
import org.example.airport.exceptions.UsernameAlreadyExistsException;
import org.example.airport.repository.UserRepository;
import org.example.airport.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.example.airport.enums.TravelClass.BUSINESS;
import static org.example.airport.enums.TravelClass.ECONOMY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    public void shouldRegisterNewUser() {
        RegisterRequest request = new RegisterRequest("john", "password123", "USER", 12.0, BUSINESS);

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest("existingUser", "password", "USER", 10.0, ECONOMY);

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }
}