package com.testApp.testApp.controllers;

import com.testApp.testApp.models.Home;
import com.testApp.testApp.services.HomeService;
import com.testApp.testApp.utils.HomeRegistrationRequest;
import com.testApp.testApp.utils.HomeUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/homes")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService service;

    @GetMapping
    public List<Home> allHomes(){
        return service.findAllHomes();
    }

    @GetMapping("/{id}")
    public Home findOne(
            @PathVariable("id") Long id
    ){
        return service.findHomeById(id);
    }


    @PostMapping
    public void registerHome(
            @RequestBody HomeRegistrationRequest registrationRequest
    ){

        service.registerHome(registrationRequest);
    }


    @PutMapping("/{id}")
    public void updateHome(
            @PathVariable("id") Long id,
            @RequestBody HomeUpdateRequest request
    ){
        service.updateHome(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteHome(
            @PathVariable("id") Long id
    ){
        service.deleteHomeById(id);
    }
}
