package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.payload.CreateUserRequest;
import com.bdr.jang.entities.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public User mapToEntity(CreateUserRequest req) {
        return User.builder()
                .email(req.getEmail())
                .username(req.getUsername())
                .password(req.getPassword())
                .build();
    }
}
