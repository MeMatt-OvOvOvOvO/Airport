package org.example.airport.tests;

import org.example.airport.entity.User;
import org.example.airport.repository.UserRepository;
import org.example.airport.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void shouldLoadUserByUsername() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("john");

        assertEquals("john", result.getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });

        verify(userRepository).findByUsername("nonexistent");
    }
}