package com.bdr.jang.controller;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.model.CreateUserRequest;
import com.bdr.jang.service.UserService;
import com.bdr.jang.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController (JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid CreateUserRequest req) {
        UserDTO created = userService.register(req);
        URI location = URI.create("/users/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }
}
