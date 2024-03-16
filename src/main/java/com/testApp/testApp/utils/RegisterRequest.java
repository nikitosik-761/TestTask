package com.testApp.testApp.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record RegisterRequest(
        String username,

        int age,
        String password
){}


