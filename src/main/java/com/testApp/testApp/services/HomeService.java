package com.testApp.testApp.services;

import com.testApp.testApp.exceptions.ResourceNotFound;
import com.testApp.testApp.models.Home;
import com.testApp.testApp.repositories.HomeRepo;
import com.testApp.testApp.utils.HomeRegistrationRequest;
import com.testApp.testApp.utils.HomeUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepo homeRepo;



    public List<Home> findAllHomes(){
        return homeRepo.findAll();
    }


    public Home findHomeById(Long id){
        return homeRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Not Found")
        );

    }


    public void deleteHomeById(Long id){
        if (!existsHomeWithId(id)){
            throw new ResourceNotFound("There's no home with id [%s]".formatted(id));
        }

        homeRepo.deleteById(id);

    }

    public boolean existsHomeWithId(Long id) {
        return homeRepo.existsHomeById(id);
    }


    public void updateHome(Long id, HomeUpdateRequest requestUpdated) {
        Home home = homeRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Not found")
        );

        boolean changed = false;

        if (requestUpdated.address() != null && !requestUpdated.address().equals(home.getAddress())) {
            home.setAddress(requestUpdated.address());
            changed = true;
        }

        if (!changed){
            throw new RuntimeException("No changes were found");
        }

        homeRepo.save(home);

    }

    public void registerHome(HomeRegistrationRequest registrationRequest) {

       Home home = Home
               .builder()
               .address(registrationRequest.address())
               .host(registrationRequest.user())
               .build();

       homeRepo.save(home);

    }
}
