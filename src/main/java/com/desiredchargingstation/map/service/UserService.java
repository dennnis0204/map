package com.desiredchargingstation.map.service;

import com.desiredchargingstation.map.dto.UserDto;
import com.desiredchargingstation.map.model.User;

import javax.validation.constraints.Email;

public interface UserService {

    User getUserByEmail(@Email String email);

    UserDto getUser(String email);
}
