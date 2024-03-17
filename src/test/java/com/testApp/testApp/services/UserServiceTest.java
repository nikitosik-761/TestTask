package com.testApp.testApp.services;

import com.testApp.testApp.exceptions.ResourceNotFound;
import com.testApp.testApp.models.Home;
import com.testApp.testApp.models.User;
import com.testApp.testApp.repositories.HomeRepo;
import com.testApp.testApp.repositories.UserRepo;
import com.testApp.testApp.utils.AddUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//Тесты очень аналогичны HomeService тестам. Наиболее интересен тест метода addUser()
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService underTest;

    @Mock
    private UserRepo userRepo;
    @Mock
    private HomeRepo homeRepo;


    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepo,homeRepo);
    }

    @Test
    void addUser() {
        Long idUserToAdd = 10L;

        User host = new User(
                1L,
                "host",
                21,
                "password"
        );



        User userToBeAdded = new User(
                43L,
                "user",
                21,
                "password"
        );

        Home home = new Home(
                1L,
                "addrees",
                host,
                Collections.emptySet()
        );

        AddUserRequest request = new AddUserRequest(host, idUserToAdd);

        when(homeRepo.findHomeByHost(host)).thenReturn(Optional.of(home));
        when(userRepo.findById(idUserToAdd)).thenReturn(Optional.of(userToBeAdded));

        underTest.addUser(
                request
        );

        verify(homeRepo).save(home);


        assertThat(home.getUsers().contains(userToBeAdded)).isTrue();

    }

    @Test
    void addUserWhenUpdatedUserSetIsNotEmpty() {
        Long idUserToAdd = 10L;

        User host = new User(
                1L,
                "host",
                21,
                "password"
        );



        User userToBeAdded = new User(
                43L,
                "user",
                21,
                "password"
        );


        Home home = new Home(
                1L,
                "addrees",
                host,
                Set.of(new User(54L,"user", 34,"password"))
        );



        AddUserRequest request = new AddUserRequest(host, idUserToAdd);

        when(homeRepo.findHomeByHost(host)).thenReturn(Optional.of(home));
        when(userRepo.findById(idUserToAdd)).thenReturn(Optional.of(userToBeAdded));

        underTest.addUser(
                request
        );

        verify(homeRepo).save(home);

        assertThat(home.getUsers().size()).isEqualTo(2);
        assertThat(home.getUsers().contains(userToBeAdded)).isTrue();


    }


    @Test
    void addUserWillThrowIfNotTheHost() {
        Long idUserToAdd = 10L;

        User host = new User(
                1L,
                "host",
                21,
                "password"
        );

        AddUserRequest request = new AddUserRequest(host, idUserToAdd);

        when(homeRepo.findHomeByHost(host)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> underTest.addUser(request))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Home with host id [%s] not found".formatted(host.getId()));

        verify(homeRepo, never()).save(any());

    }

    @Test
    void addUserWllThrowWhenUserToAddDoesNotExist() {
        Long idUserToAdd = 10L;

        User host = new User(
                1L,
                "host",
                21,
                "password"
        );



        Home home = new Home(
                1L,
                "addrees",
                host,
                Collections.emptySet()
        );

        AddUserRequest request = new AddUserRequest(host, idUserToAdd);

        when(homeRepo.findHomeByHost(host)).thenReturn(Optional.of(home));
        when(userRepo.findById(idUserToAdd)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.addUser(request))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Not found");

        verify(homeRepo, never()).save(any());

    }

}