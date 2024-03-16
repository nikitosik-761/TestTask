package com.testApp.testApp.utils;

import com.testApp.testApp.models.User;

public record AddUserRequest(
        User host,
        Long userToAddId
) {
}
