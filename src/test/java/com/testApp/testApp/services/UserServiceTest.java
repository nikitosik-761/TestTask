package com.testApp.testApp.services;

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

import static org.junit.jupiter.api.Assertions.*;

//Тесты очень похожи на тесты HomeService тест. Наиболее интересен тест метода addUser()
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
                "password",
                Collections.emptyList()
        );



        User userToBeAdded = new User(
                43L,
                "user",
                21,
                "password",
                Collections.emptyList()
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

    //Todo добавить тесты которые падают если мы не хозяин дома
}