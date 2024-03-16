package com.testApp.testApp.controllers;

import com.testApp.testApp.models.User;
import com.testApp.testApp.services.UserService;
import com.testApp.testApp.utils.AddUserRequest;
import com.testApp.testApp.utils.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> allUsers(){
        return service.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findOne(
            @PathVariable("id") Long id
    ){
        return service.findUserById(id);
    }


    @PutMapping("/{id}")
    public void updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserUpdateRequest request
    ){
        service.updateUser(id, request);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable("id") Long id
    ){
        service.deleteUserById(id);
    }

    @PostMapping("/add")
    public void addUser(
            @RequestBody AddUserRequest request
    ){
        service.addUser(request);
    }


}
