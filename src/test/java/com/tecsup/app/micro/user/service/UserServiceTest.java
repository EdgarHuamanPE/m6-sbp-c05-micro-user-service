package com.tecsup.app.micro.user.service;

import com.tecsup.app.micro.user.dto.User;
import com.tecsup.app.micro.user.entity.UserEntity;
import com.tecsup.app.micro.user.mapper.UserMapper;
import com.tecsup.app.micro.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void getUserById() {
        Long ID = 100L;
        String NAME = "Jaime";
        String EMAIL = "jaime@demo.com";

        User existingUser = User.builder()
                .id(ID)
                .name(NAME)
                .email(EMAIL)
                .build(); // new User(ID, NAME, EMAIL);

        when(userRepository.findById(100L)).thenReturn(Optional.of(userMapper.toEntity(existingUser)));

        User realUser = userService.getUserById(100L);

        assertNotNull(realUser);

        // hope values, real values
        assertEquals(ID, realUser.getId());
        assertEquals(NAME, realUser.getName());
        assertEquals(EMAIL, realUser.getEmail());
    }


    @Test
    void getAllUsers() {

        User user1 = User.builder().id(1L).name("A").email("a@test.com").build();
        User user2 = User.builder().id(2L).name("B").email("b@test.com").build();

        when(userRepository.findAll()).thenReturn(
                List.of(
                        userMapper.toEntity(user1),
                        userMapper.toEntity(user2)
                )
        );

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void saveUser() {

        User newUser = User.builder()
                .name("Carlos")
                .email("carlos@test.com")
                .phone("999999999")
                .address("Lima")
                .build();

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> {
                    UserEntity entity = invocation.getArgument(0);
                    entity.setId(10L);
                    return entity;
                });

        User saved = userService.saveUser(newUser);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
        assertEquals("Carlos", saved.getName());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteUser() {

        User user = User.builder()
                .id(5L)
                .name("DeleteMe")
                .email("delete@test.com")
                .build();

        userService.deleteUser(user);

        verify(userRepository, times(1))
                .delete(userMapper.toEntity(user));
    }
}
