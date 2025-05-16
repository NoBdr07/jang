package com.bdr.jang.controller;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.payload.AuthResponse;
import com.bdr.jang.entities.payload.CreateUserRequest;
import com.bdr.jang.entities.payload.LoginRequest;
import com.bdr.jang.service.UserService;
import com.bdr.jang.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * REST controller for authentication operations
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Registers a new user
     * @param req CreateUserRequest that contains email, username and password
     * @return 201 with new UserDTO when register succeeds
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid CreateUserRequest req) {
        UserDTO created = userService.register(req);
        URI location = URI.create("/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Login a known user
     * @param req LoginRequest that contains username and password
     * @param response HttpServletResponse
     * @return 200 with AuthResponse that contains jwt and ExpireIn when login succeeds
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req, HttpServletResponse response) {
        String token = userService.login(req);
        Cookie cookie = jwtUtils.createCookie("jwt", token, 24 * 60 * 60, false);
        response.addCookie(cookie);
        AuthResponse body = new AuthResponse(token, 86400L);
        return ResponseEntity.ok(body);
    }
}
