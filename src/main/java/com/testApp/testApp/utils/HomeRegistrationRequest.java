package com.testApp.testApp.utils;

import com.testApp.testApp.models.User;

public record HomeRegistrationRequest(
        String address,
        User user
) {
}
