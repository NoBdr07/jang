package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.payload.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    User user;
    UserDTO userDTO;
    CreateUserRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@mail.com")
                .username("name")
                .password("password")
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .email("test@mail.com")
                .username("name")
                .build();

    }

    @Test
    void mapToDto() {
        // WHEN
        UserDTO result = userMapper.mapToDto(user);

        // THEN
        assertEquals(userDTO, result);
    }

    @Test
    void mapToEntity() {
        // GIVEN
        request = CreateUserRequest.builder()
                .email("test@mail.com")
                .username("name")
                .password("password")
                .build();

        // WHEN
        User result = userMapper.mapToEntity(request);

        // THEN
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }
}