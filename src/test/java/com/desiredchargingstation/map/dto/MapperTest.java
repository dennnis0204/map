package com.desiredchargingstation.map.dto;

import com.desiredchargingstation.map.model.AuthProvider;
import com.desiredchargingstation.map.model.ChargingPoint;
import com.desiredchargingstation.map.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MapperTest {

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private UserPointsMapper userPointsMapper = Mappers.getMapper(UserPointsMapper.class);
    private ChargingPointMapper chargingPointMapper = Mappers.getMapper(ChargingPointMapper.class);

    private User user;
    private ChargingPoint firstPoint;
    private ChargingPoint secondPoint;
    private ChargingPointDto chargingPointDto;

    @BeforeAll
    public void beforeAll() {
        log.info("beforeAll");
        user = new User();
        user.setName("Jim");
        user.setEmail("jim@gmail.com");
        user.setProvider(AuthProvider.google);

        firstPoint = new ChargingPoint();
        firstPoint.setId(1L);
        firstPoint.setLatitude(new BigDecimal("33.33"));
        firstPoint.setLongitude(new BigDecimal("44.44"));

        secondPoint = new ChargingPoint();
        secondPoint.setId(2L);
        secondPoint.setLatitude(new BigDecimal("88.88"));
        secondPoint.setLongitude(new BigDecimal("77.77"));

        user.setChargingPoints(List.of(firstPoint, secondPoint));

        chargingPointDto = new ChargingPointDto();
        chargingPointDto.setId(1L);
        chargingPointDto.setLatitude(new BigDecimal("33.33"));
        chargingPointDto.setLongitude(new BigDecimal("44.44"));
    }

    @AfterAll
    public void tearDown() {
        log.info("tearDown");
        user = null;
    }

    @Test
    public void givenUserToUserDto_whenMaps_thenCorrect() {
        UserDto userDto = userMapper.userToUserDto(user);

        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getName()).isEqualTo(user.getName());
    }

    @Test
    public void givenChargingPointToChargingPointDto_whenMaps_thenCorrect() {
        ChargingPointDto chargingPointDto = chargingPointMapper.chargingPointToChargingPointDto(firstPoint);

        assertThat(chargingPointDto.getId()).isEqualTo(firstPoint.getId());
        assertThat(chargingPointDto.getLatitude()).isEqualTo(firstPoint.getLatitude());
        assertThat(chargingPointDto.getLongitude()).isEqualTo(firstPoint.getLongitude());
    }

    @Test
    public void givenChargingPointDtoToChargingPoint_whenMaps_thenCorrect() {
        ChargingPoint chargingPoint = chargingPointMapper.chargingPointDtoToChargingPoint(chargingPointDto);

        assertThat(chargingPoint.getId()).isEqualTo(chargingPointDto.getId());
        assertThat(chargingPoint.getLatitude()).isEqualTo(chargingPointDto.getLatitude());
        assertThat(chargingPoint.getLongitude()).isEqualTo(chargingPointDto.getLongitude());
        assertThat(firstPoint).isEqualTo(chargingPoint);
    }

    @Test
    public void givenUser_whenMaps_thenCorrect() {
        UserPointsDto userPointsDto = userPointsMapper.userToUserPointsDto(user);

        assertThat(userPointsDto.getName().equals("Jim"));
        assertThat(userPointsDto.getEmail().equals("jim@gmail.com"));
        assertThat(userPointsDto.getChargingPoints().size() == 2);
        assertThat(userPointsDto.getChargingPoints().get(0).getLatitude().equals(new BigDecimal("33.33")));
        assertThat(userPointsDto.getChargingPoints().get(0).getLongitude().equals(new BigDecimal("44.44")));
    }
}
