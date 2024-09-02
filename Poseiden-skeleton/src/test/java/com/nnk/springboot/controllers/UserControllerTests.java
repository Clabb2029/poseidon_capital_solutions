package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.UsernameAlreadyExistingException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "Admintest", password = "passwordtest", roles = "ADMIN")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User(1, "user1", "password1", "user 1", "USER");
        user2 = new User(2, "user2", "password2", "user 2", "USER");
    }

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("user/list"));
    }

    @Test
    public void addUserTest() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void validateTest() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("fullname", user1.getFullname())
                        .param("username", user1.getUsername())
                        .param("password", user1.getPassword())
                        .param("role", user1.getRole())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void validateButUsernameAlreadyTakenTest() throws Exception {
        doThrow(new UsernameAlreadyExistingException("Username already taken.")).when(userService).createUser(any(User.class));
        mockMvc.perform(post("/user/validate")
                        .param("fullname", user1.getFullname())
                        .param("username", user1.getUsername())
                        .param("password", user1.getPassword())
                        .param("role", user1.getRole())
                )
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/add"));
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    public void validateWhenFormNotValidTest() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("fullname", "")
                        .param("username", "")
                        .param("password", "")
                        .param("role", "")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
        verify(userService, times(0)).createUser(any(User.class));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user1));
        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/update"));
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void updateUserTest() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .param("fullname", user2.getFullname())
                        .param("username", user2.getUsername())
                        .param("password", user2.getPassword())
                        .param("role", user2.getRole())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUserWhenFormNotValidTest() throws Exception {
        mockMvc.perform(post("/user/update/1")
                        .param("fullname", "")
                        .param("username", "")
                        .param("password", "")
                        .param("role", "")
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("users"))
                .andExpect(view().name("user/update"));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void deleteUserTest() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user1));
        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }
}