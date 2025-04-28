package com.bdr.jang.controller;

import com.bdr.jang.dto.UserDTO;
import com.bdr.jang.model.CreateUserRequest;
import com.bdr.jang.service.UserService;
import com.bdr.jang.util.JwtUtils;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
