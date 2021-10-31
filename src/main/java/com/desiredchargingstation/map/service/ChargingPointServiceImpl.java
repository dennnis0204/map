package com.desiredchargingstation.map.service;

import com.desiredchargingstation.map.dto.*;
import com.desiredchargingstation.map.exception.ChargingPointDoesNotExistException;
import com.desiredchargingstation.map.exception.UserMaxPointsNumberExceededException;
import com.desiredchargingstation.map.model.ChargingPoint;
import com.desiredchargingstation.map.model.User;
import com.desiredchargingstation.map.repository.ChargingPointRepository;
import com.desiredchargingstation.map.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargingPointServiceImpl implements ChargingPointService {

    @Value("${settings.user.maxPointsNumber}")
    private Integer userMaxPointsNumber;

    private static final String USER_MAX_POINTS_NUMBER_EXCEEDED_MESSAGE = "User already has %s charging points on the map!";
    private static final String USER_HAS_NO_SUCH_POINT_MESSAGE = "User has no such point!";

    private final UserService userService;
    private final UserRepository userRepository;
    private final ChargingPointRepository chargingPointRepository;
    private final UserPointsMapper userPointsMapper;
    private final ChargingPointMapper chargingPointMapper;
    private final Validator validator;

    @Override
    public List<ChargingPointDto> getAllChargingPoints() {
        return chargingPointRepository.findAll().stream()
                .map(point -> chargingPointMapper.chargingPointToChargingPointDto(point))
                .collect(Collectors.toList());
    }

    @Override
    public void addUserPoint(String email, @Valid NewChargingPointDto newChargingPointDto) {
        User user = userService.getUserByEmail(email);
        ChargingPoint newChargingPoint = chargingPointMapper.newChargingPointDtoToChargingPoint(newChargingPointDto);
        validateChargingPoint(newChargingPoint);

        if (user.getChargingPoints().size() < userMaxPointsNumber) {
            user.addChargingPoint(newChargingPoint);
            userRepository.save(user);
        } else {
            throw new UserMaxPointsNumberExceededException(
                    String.format(USER_MAX_POINTS_NUMBER_EXCEEDED_MESSAGE, userMaxPointsNumber));
        }
    }

    @Override
    public void deleteUserPoint(String email, @Valid ChargingPointDto chargingPointDto) {
        User user = userService.getUserByEmail(email);
        ChargingPoint newChargingPoint = chargingPointMapper.chargingPointDtoToChargingPoint(chargingPointDto);
        validateChargingPoint(newChargingPoint);

        if (user.hasChargingPoint(newChargingPoint)) {
            user.removeChargingPoint(newChargingPoint);
            userRepository.save(user);
        } else {
            throw new ChargingPointDoesNotExistException(USER_HAS_NO_SUCH_POINT_MESSAGE);
        }
    }

    @Override
    public void updateUserPoint(String email, @Valid ChargingPointDto chargingPointDto) {
        User user = userService.getUserByEmail(email);
        ChargingPoint chargingPoint = chargingPointMapper.chargingPointDtoToChargingPoint(chargingPointDto);
        validateChargingPoint(chargingPoint);
        if (user.hasChargingPoint(chargingPoint)) {
            user.updateChargingPoint(chargingPoint);
            userRepository.save(user);
        } else {
            throw new ChargingPointDoesNotExistException(USER_HAS_NO_SUCH_POINT_MESSAGE);
        }
    }

    @Override
    public UserPointsDto getUserPoints(String email) {
        User user = userService.getUserByEmail(email);
        UserPointsDto userPointsDto = userPointsMapper.userToUserPointsDto(user);
        return userPointsDto;
    }

    private void validateChargingPoint(ChargingPoint newChargingPoint) {
        Set<ConstraintViolation<ChargingPoint>> constraintViolations = validator.validate(newChargingPoint);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
