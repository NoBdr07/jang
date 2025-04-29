package com.bdr.jang.service;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.model.CreateUserRequest;
import com.bdr.jang.entities.model.LoginRequest;
import jakarta.servlet.http.Cookie;

public interface UserService {

    UserDTO register(CreateUserRequest req);
    Cookie login(LoginRequest req);

}
