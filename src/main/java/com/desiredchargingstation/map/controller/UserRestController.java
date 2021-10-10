package com.desiredchargingstation.map.controller;

import com.desiredchargingstation.map.dto.UserDto;
import com.desiredchargingstation.map.security.UserPrincipal;
import com.desiredchargingstation.map.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/user")
    public UserDto getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getUser(userPrincipal.getEmail());
    }
}
