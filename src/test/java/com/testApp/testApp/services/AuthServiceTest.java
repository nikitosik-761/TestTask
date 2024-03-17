package com.testApp.testApp.services;

import com.testApp.testApp.models.User;
import com.testApp.testApp.repositories.UserRepo;
import com.testApp.testApp.utils.AuthenticationRequest;
import com.testApp.testApp.utils.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private AuthService underTest;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        underTest = new AuthService(userRepo, passwordEncoder, jwtService,authenticationManager);
    }

    @Test
    void register() {
        RegisterRequest request = new RegisterRequest(
                "name",
                23,
                "password"
        );

        underTest.register(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(
                User.class
        );

        verify(passwordEncoder).encode(request.password());

        verify(userRepo).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getId()).isNull();
        assertThat(capturedUser.getUsername()).isEqualTo(request.username());
        assertThat(capturedUser.getAge()).isEqualTo(request.age());

        verify(jwtService).generateToken(capturedUser);

    }

    @Test
    void authenticate() {


        AuthenticationRequest request = new AuthenticationRequest(
                "user",
                "password"
        );

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );


        User user = new User(
                1L,
                "user",
                23,
                "password"
        );

        Authentication mockAuthentication = mock(Authentication.class);



        when(authenticationManager.authenticate(token)).thenReturn(mockAuthentication);
        when(userRepo.findByUsername(request.username())).thenReturn(Optional.of(user));

        underTest.authenticate(request);

        verify(authenticationManager).authenticate(
                token
        );

        verify(jwtService).generateToken(user);
    }

    @Test
    void authenticateWillThrow() {


        AuthenticationRequest request = new AuthenticationRequest(
                "user",
                "password"
        );

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );


        User user = new User(
                1L,
                "user",
                23,
                "password"
        );

        Authentication mockAuthentication = mock(Authentication.class);



        when(authenticationManager.authenticate(token)).thenReturn(mockAuthentication);
        when(userRepo.findByUsername(request.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.authenticate(request))
                .isInstanceOf(RuntimeException.class)
                        .hasMessage("Not found");

        verify(jwtService, never()).generateToken(user);
    }
}