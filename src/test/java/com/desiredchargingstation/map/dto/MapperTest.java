package com.desiredchargingstation.map.dto;

import com.desiredchargingstation.map.model.AuthProvider;
import com.desiredchargingstation.map.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MapperTest {

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private User user;

    @BeforeAll
    public void beforeAll() {
        log.info("beforeAll");
        user = new User();

                user.setName("Jim");
                user.setEmail("jim@gmail.com");
                user.setProvider(AuthProvider.google);
    }

    @Test
    public void givenUserToUserDto_whenMaps_thenCorrect() {
        UserDto userDto = userMapper.userToUserDto(user);
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getName()).isEqualTo(user.getName());
    }

}
