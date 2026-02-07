package com.tecsup.app.micro.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.app.micro.user.dto.CreateRequestUser;
import com.tecsup.app.micro.user.dto.User;
import com.tecsup.app.micro.user.entity.UserEntity;
import com.tecsup.app.micro.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    // Object Mapper
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserById() throws Exception {

        String NAME = "Juan Pérez";
        String EMAIL = "juan.perez@example.com";

        this.mockMvc.perform(get("/api/users/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.email", is(EMAIL)));
    }

    @Test
    void getAllUsers() throws Exception {
        int NRO_RECORD = 6;
        final int ID_FIRST_RECORD = 1;

        this.mockMvc.perform(get("/api/users/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(NRO_RECORD)))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));

    }

    @Test
    void createUser() throws Exception {

        CreateRequestUser request = new CreateRequestUser();
        request.setName("Carlos");
        request.setEmail("carlos@test.com");
        request.setPhone("999999999");
        request.setAddress("Lima");

        this.mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Carlos")))
                .andExpect(jsonPath("$.email", is("carlos@test.com")));
    }

    @Test
    void deleteUser() throws Exception {


        UserEntity entity = new UserEntity();
        entity.setName("DeleteMe");
        entity.setEmail("delete@test.com");
        entity.setPhone("999999999");
        entity.setAddress("Lima");

        entity = userRepository.save(entity);
        Long id = entity.getId();

        this.mockMvc.perform(delete("/api/users/" + id))
                .andDo(print())
                .andExpect(status().isOk());

        boolean exists = userRepository.findById(id).isPresent();

        assertFalse(exists);
    }


}