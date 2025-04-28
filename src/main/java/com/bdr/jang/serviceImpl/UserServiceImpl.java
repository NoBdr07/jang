package com.bdr.jang.serviceImpl;

import com.bdr.jang.dto.UserDTO;
import com.bdr.jang.mapper.UserMapper;
import com.bdr.jang.model.CreateUserRequest;
import com.bdr.jang.model.User;
import com.bdr.jang.repository.UserRepository;
import com.bdr.jang.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl (UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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
                    "Email ou nom d’utilisateur déjà utilisé"
            );
        }
    }
}
