package com.desiredchargingstation.map.service;

import com.desiredchargingstation.map.dto.UserDto;
import com.desiredchargingstation.map.dto.UserMapper;
import com.desiredchargingstation.map.model.User;
import com.desiredchargingstation.map.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND_MESSAGE = "User not found with email: ";

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getUserByEmail(@Email String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND_MESSAGE + email));
    }

    @Override
    public UserDto getUser(String email) {
        User user = getUserByEmail(email);
        UserDto userDto = userMapper.userToUserDto(user);
        return userDto;
    }
}
