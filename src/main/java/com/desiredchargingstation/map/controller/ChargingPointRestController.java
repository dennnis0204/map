package com.desiredchargingstation.map.controller;

import com.desiredchargingstation.map.dto.ChargingPointDto;
import com.desiredchargingstation.map.dto.NewChargingPointDto;
import com.desiredchargingstation.map.dto.UserPointsDto;
import com.desiredchargingstation.map.security.UserPrincipal;
import com.desiredchargingstation.map.service.ChargingPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChargingPointRestController {

    private final ChargingPointService chargingPointService;

    @GetMapping("/all-points")
    public List<ChargingPointDto> getAllChargingPoints() {
        return chargingPointService.getAllChargingPoints();
    }

    @GetMapping("/points")
    public UserPointsDto getUserPoints(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return chargingPointService.getUserPoints(userPrincipal.getEmail());
    }

    @PostMapping("/points")
    public UserPointsDto addUserPoint(
            @RequestBody @Valid NewChargingPointDto newChargingPointDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        chargingPointService.addUserPoint(userPrincipal.getEmail(), newChargingPointDto);
        return chargingPointService.getUserPoints(userPrincipal.getEmail());
    }

    @DeleteMapping("/points")
    public UserPointsDto deleteUserPoint(
            @RequestBody @Valid ChargingPointDto chargingPointDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        chargingPointService.deleteUserPoint(userPrincipal.getEmail(), chargingPointDto);
        return chargingPointService.getUserPoints(userPrincipal.getEmail());
    }

    @PutMapping("/points")
    public UserPointsDto updateUserPoint(
            @RequestBody @Valid ChargingPointDto chargingPointDto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        chargingPointService.updateUserPoint(userPrincipal.getEmail(), chargingPointDto);
        return chargingPointService.getUserPoints(userPrincipal.getEmail());
    }
}
