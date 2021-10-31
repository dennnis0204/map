package com.desiredchargingstation.map.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import java.util.List;

@Data
public class UserPointsDto {

    private String name;

    @Email
    private String email;

    private List<ChargingPointDto> chargingPoints;
}
