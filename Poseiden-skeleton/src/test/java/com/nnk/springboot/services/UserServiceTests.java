package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void createUserTest() {
        User user = new User(1, "Username", "Password", "Full Name", "USER");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        userService.createUser(user);
        verify(passwordEncoder, times(1)).encode("Password");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void createUserExceptionTest() {
        User user = new User(1, "Username", "Password", "Full Name", "USER");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        try {
            userService.createUser(user);
            fail();
        } catch (Exception e) {
            assertEquals(e.getMessage(), "An account with this username already exists. Please use another username.");
        }
        verify(userRepository, times(0)).save(user);
    }
}
