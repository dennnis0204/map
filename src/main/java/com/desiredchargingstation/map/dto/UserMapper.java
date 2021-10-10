package com.desiredchargingstation.map.dto;

import com.desiredchargingstation.map.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);
}
