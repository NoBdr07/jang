package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.mapper.UserMapper;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.payload.CreateUserRequest;
import com.bdr.jang.entities.payload.LoginRequest;
import com.bdr.jang.exception.EmailAlreadyTakenException;
import com.bdr.jang.exception.UsernameAlreadyTakenException;
import com.bdr.jang.repository.UserRepository;
import com.bdr.jang.service.UserService;
import com.bdr.jang.util.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDTO register(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new UsernameAlreadyTakenException("This username is already taken");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyTakenException("This email is already taken");
        }

        User user = userMapper.mapToEntity(req);
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return userMapper.mapToDto(saved);

    }

    @Override
    public String login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.username(),
                        req.password()
                )
        );

        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No role found for this user"));

        return jwtUtils.generateToken(auth.getName(), role);
    }

    @Override
    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur non trouvé pour id = " + id));
    }

    @Override
    public UserDTO findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur non trouvé pour id = " + id));
    }

    @Override
    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur non trouvé pour username = " + username
                ));
    }

}
