package com.desiredchargingstation.map.service;

import com.desiredchargingstation.map.dto.ChargingPointDto;
import com.desiredchargingstation.map.dto.NewChargingPointDto;
import com.desiredchargingstation.map.dto.UserPointsDto;

import javax.validation.Valid;
import java.util.List;

public interface ChargingPointService {

    void addUserPoint(String email, @Valid NewChargingPointDto newChargingPointDto);

    void deleteUserPoint(String email, @Valid ChargingPointDto chargingPointDto);

    void updateUserPoint(String email, @Valid ChargingPointDto chargingPointDto);

    UserPointsDto getUserPoints(String email);

    List<ChargingPointDto> getAllChargingPoints();
}
