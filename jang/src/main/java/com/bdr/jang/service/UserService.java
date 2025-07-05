package com.bdr.jang.service;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.payload.CreateUserRequest;
import com.bdr.jang.entities.payload.LoginRequest;
import jakarta.servlet.http.Cookie;

public interface UserService {

    UserDTO register(CreateUserRequest req);
    String login(LoginRequest req);
    UserDTO findUserById(Long id);
    User    findUserEntityById(Long id);
}
