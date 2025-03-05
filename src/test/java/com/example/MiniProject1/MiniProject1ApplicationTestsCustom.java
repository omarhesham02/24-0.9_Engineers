package com.example.MiniProject1;

import com.example.controller.UserController;
import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserServiceTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Test
    void testGetUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        Mockito.when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

@Test
void testGetAllUsers() throws Exception {
    User user1 = new User();
    user1.setId(UUID.randomUUID());
    user1.setName("John Doe1");

    User user2 = new User();
    user2.setId(UUID.randomUUID());
    user2.setName("John Doe2");

    User user3 = new User();
    user3.setId(UUID.randomUUID());
    user3.setName("John Doe3");

    Mockito.when(userService.getUsers()).thenReturn(new ArrayList<>(Arrays.asList(user1, user2, user3)));

    mockMvc.perform(MockMvcRequestBuilders.get("/user/"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(user1.getId().toString()))
            .andExpect(jsonPath("$[0].name").value("John Doe1"))
            .andExpect(jsonPath("$[1].id").value(user2.getId().toString()))
            .andExpect(jsonPath("$[1].name").value("John Doe2"))
            .andExpect(jsonPath("$[2].id").value(user3.getId().toString()))
            .andExpect(jsonPath("$[2].name").value("John Doe3"));
}

@Test
void testCreateUser() throws Exception {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "John Doe");

    Mockito.when(userService.addUser(Mockito.any(User.class))).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/user/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\": \"" + userId.toString() + "\", \"name\": \"John Doe\"}"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value("John Doe"));
}

    @Test
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.doNothing().when(userService).deleteUserById(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id}", userId))
                .andExpect(status().isNoContent());
    }
}