package com.desiredchargingstation.map.dto;

import com.desiredchargingstation.map.model.ChargingPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChargingPointMapper {

    @Mapping(target = "user", ignore = true)
    ChargingPoint chargingPointDtoToChargingPoint(ChargingPointDto chargingPointDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    ChargingPoint newChargingPointDtoToChargingPoint(NewChargingPointDto newChargingPointDto);

    ChargingPointDto chargingPointToChargingPointDto(ChargingPoint chargingPoint);
}
