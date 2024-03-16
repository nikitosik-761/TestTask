package com.testApp.testApp.services;

import com.testApp.testApp.exceptions.ResourceNotFound;
import com.testApp.testApp.models.Home;
import com.testApp.testApp.models.User;
import com.testApp.testApp.repositories.HomeRepo;
import com.testApp.testApp.utils.HomeRegistrationRequest;
import com.testApp.testApp.utils.HomeUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    private HomeService underTest;

    @Mock
    private HomeRepo homeRepo;

    @BeforeEach
    void setUp() {
        underTest = new HomeService(homeRepo);
    }

    @Test
    void findAllHomes() {
        underTest.findAllHomes();

        verify(homeRepo).findAll();
    }

    @Test
    void findHomeById() {
        Long id = 1L;

        Home home = Home
                .builder()
                .id(id)
                .address("Address")
                .build();

        when(homeRepo.findById(id)).thenReturn(Optional.of(home));


        Home actual = underTest.findHomeById(id);

        assertThat(actual).isEqualTo(home);
    }

    @Test
    void findHomeByIdWillThrowWhenReturnsEmptyOptional() {
        Long id = 1L;
        when(homeRepo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findHomeById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Not Found");

    }

    @Test
    void deleteHomeById() {
        Long id = 10L;
        when(homeRepo.existsHomeById(id)).thenReturn(true);

        underTest.deleteHomeById(id);

        verify(homeRepo).deleteById(id);
    }

    @Test
    void deleteHomeByIdWillThrowWhenNotFound() {
        Long id = 10L;
        when(homeRepo.existsHomeById(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteHomeById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("There's no home with id [%s]".formatted(id));

        verify(homeRepo, never()).deleteById(id);

    }

    @Test
    void updateHomeAllProperties() {
        Long id = 1L;

        Home home = Home
                .builder()
                .id(id)
                .address("Address")
                .build();

        when(homeRepo.findById(id)).thenReturn(Optional.of(home));

        String newAddress = "newAddress";

        HomeUpdateRequest request = new HomeUpdateRequest(
                newAddress
        );


        underTest.updateHome(id, request);




        ArgumentCaptor<Home> homeArgumentCaptor =
                ArgumentCaptor.forClass(Home.class);

        verify(homeRepo).save(homeArgumentCaptor.capture());

        Home capturedHome = homeArgumentCaptor.getValue();

        assertThat(capturedHome.getAddress()).isEqualTo(request.address());

    }




    @Test
    void willThrowWhenHomeUpdateNoChanges() {

        Long id = 1L;

        Home home = Home
                .builder()
                .id(id)
                .address("Address")
                .build();

        when(homeRepo.findById(id)).thenReturn(Optional.of(home));


        HomeUpdateRequest request = new HomeUpdateRequest(
                home.getAddress()
        );


        assertThatThrownBy(() -> underTest.updateHome(id, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No changes were found");


        verify(homeRepo, never()).save(any());

    }

    @Test
    void updateHomeWillThrowWhenIsNotFound() {

        Long id = -1L;

        when(homeRepo.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateHome(id, any()))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("Not found");

        verify(homeRepo, never()).save(any());

    }

    @Test
    void registerHome() {

        User user = User.builder()
                .username("name")
                .age(21)
                .build();

        HomeRegistrationRequest request = new HomeRegistrationRequest(
                "address", user

        );

        underTest.registerHome(request);

        ArgumentCaptor<Home> homeArgumentCaptor = ArgumentCaptor.forClass(
                Home.class
        );

        verify(homeRepo).save(homeArgumentCaptor.capture());

        Home capturedHome = homeArgumentCaptor.getValue();

        assertThat(capturedHome.getId()).isNull();
        assertThat(capturedHome.getAddress()).isEqualTo(request.address());
        assertThat(capturedHome.getHost()).isEqualTo(request.user());



    }
}