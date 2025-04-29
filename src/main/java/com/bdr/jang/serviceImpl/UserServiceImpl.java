package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.mapper.UserMapper;
import com.bdr.jang.entities.payload.CreateUserRequest;
import com.bdr.jang.entities.payload.LoginRequest;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.repository.UserRepository;
import com.bdr.jang.service.UserService;
import com.bdr.jang.util.JwtUtils;
import jakarta.servlet.http.Cookie;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDTO register(CreateUserRequest req) {
        User user = userMapper.mapToEntity(req);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User saved = userRepository.save(user);
            return userMapper.mapToDto(saved);
        } catch (DataIntegrityViolationException ex) {
            // contrainte unique en base violée → 409 Conflict
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email or username already taken"
            );
        }
    }

    @Override
    public Cookie login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return jwtUtils.createCookie("JWT", token, 24 * 60 * 60, true);
    }

}
