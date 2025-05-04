package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.UserDTO;
import com.bdr.jang.entities.mapper.UserMapper;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.payload.CreateUserRequest;
import com.bdr.jang.entities.payload.LoginRequest;
import com.bdr.jang.exception.EmailAlreadyTakenException;
import com.bdr.jang.exception.UsernameAlreadyTakenException;
import com.bdr.jang.repository.UserRepository;
import com.bdr.jang.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtils jwtUtils;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private UserServiceImpl userService;

    private String email;
    private String username;
    private String password;
    private String encodedPassword;
    private String token;

    private CreateUserRequest req;
    private LoginRequest loginReq;
    private User userBeforeSave;
    private User userAfterSave;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        email           = "test@mail.com";
        username        = "newUser";
        password        = "pass1234";
        encodedPassword = "encodedPass1234";
        token           = "fake-jwt-token";

        req = CreateUserRequest.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        loginReq = new LoginRequest(username, password);

        userBeforeSave = User.builder()
                .email(email)
                .username(username)
                .password(password)       // brut
                .build();

        userAfterSave = User.builder()
                .id(1L)
                .email(email)
                .username(username)
                .password(encodedPassword) // hashé
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .email(email)
                .username(username)
                .build();
    }

    @Test
    void register_shouldReturnNewUserDTO_whenValid() {
        // GIVEN
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userMapper.mapToEntity(req)).thenReturn(userBeforeSave);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(userBeforeSave)).thenReturn(userAfterSave);
        when(userMapper.mapToDto(userAfterSave)).thenReturn(userDTO);

        // WHEN
        UserDTO result = userService.register(req);

        // THEN
        assertEquals(userDTO, result);
        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByEmail(email);
        verify(userMapper).mapToEntity(req);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(userBeforeSave);
        verify(userMapper).mapToDto(userAfterSave);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder, jwtUtils, authenticationManager);
    }

    @Test
    void register_shouldThrowUsernameAlreadyTakenException_whenUsernameExists() {
        // GIVEN
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // WHEN / THEN
        assertThrows(
                UsernameAlreadyTakenException.class,
                () -> userService.register(req)
        );

        verify(userRepository).existsByUsername(username);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder, jwtUtils, authenticationManager);
    }

    @Test
    void register_shouldThrowEmailAlreadyTakenException_whenEmailExists() {
        // GIVEN
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // WHEN / THEN
        assertThrows(
                EmailAlreadyTakenException.class,
                () -> userService.register(req)
        );

        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByEmail(email);
        verifyNoMoreInteractions(userRepository, userMapper, passwordEncoder, jwtUtils, authenticationManager);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        // GIVEN
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.getName()).thenReturn(username);
        when(jwtUtils.generateToken(username)).thenReturn(token);

        // WHEN
        String result = userService.login(loginReq);

        // THEN
        assertEquals(token, result);
        verify(authenticationManager).authenticate(argThat(
                tok ->
                        username.equals(tok.getPrincipal()) &&
                                password.equals(tok.getCredentials())
        ));
        verify(jwtUtils).generateToken(username);
        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtUtils, authenticationManager);
    }

    @Test
    void login_shouldThrowBadCredentialsException_whenAuthenticationFails() {
        // GIVEN
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // WHEN / THEN
        BadCredentialsException ex = assertThrows(
                BadCredentialsException.class,
                () -> userService.login(loginReq)
        );
        assertEquals("Bad credentials", ex.getMessage());

        verify(authenticationManager).authenticate(any());
        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtUtils, authenticationManager);
    }
}
