package com.testApp.testApp.services;

import com.testApp.testApp.exceptions.ResourceNotFound;
import com.testApp.testApp.models.Home;
import com.testApp.testApp.models.User;
import com.testApp.testApp.repositories.HomeRepo;
import com.testApp.testApp.repositories.UserRepo;
import com.testApp.testApp.utils.AddUserRequest;
import com.testApp.testApp.utils.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final HomeRepo homeRepo;


    public List<User> findAllUsers(){
        return userRepo.findAll();
    }


    public User findUserById(Long id){
        return userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Not Found")
        );
    }


    public void deleteUserById(Long id){
        if (!existsUserWithId(id)){
            throw new ResourceNotFound("There's no user with id [%s]".formatted(id));
        }

        userRepo.deleteById(id);

    }

    public boolean existsUserWithId(Long id) {
        return userRepo.existsUserById(id);
    }


    public void updateUser(Long id, UserUpdateRequest requestUpdated) {
        User user = userRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Not found")
        );

        boolean changed = false;

        if (requestUpdated.username() != null && !requestUpdated.username().equals(user.getUsername())) {
            user.setUsername(requestUpdated.username());
            changed = true;
        }


        if (requestUpdated.age() != null && !requestUpdated.age().equals(user.getAge())) {
            user.setAge(requestUpdated.age());
            changed = true;
        }

        if (!changed){
            throw new RuntimeException("No changes were found");
        }

        userRepo.save(user);

    }


    public void addUser(AddUserRequest request) {

        User host = request.host();


        Home home = homeRepo.findHomeByHost(host).orElseThrow(
                () -> new ResourceNotFound("Home with host id [%s] not found".formatted(host.getId()))
        );

        User userToAdd = userRepo.findById(request.userToAddId()).orElseThrow(
                () -> new ResourceNotFound("Not found")
        );

        Set<User> userSet = home.getUsers();

        userSet.add(userToAdd);

        homeRepo.save(home);

    }
}
