package com.desiredchargingstation.map.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ChargingPointDto {

    @NotNull
    @Positive(message = "Point id must be positive")
    private Long id;

    @NotNull
    @DecimalMin(value = "-90.00", message = "Min latitude value is -90.00")
    @DecimalMax(value = "90.00", message = "Max latitude value is 90.00")
    @Digits(integer = 2, fraction = 4, message = "Wrong latitude format")
    private BigDecimal latitude;

    @NotNull
    @DecimalMin(value = "-180.00", message = "Min longitude value is -180.00")
    @DecimalMax(value = "180.00", message = "Max longitude value is 180.00")
    @Digits(integer = 2, fraction = 4, message = "Wrong longitude format")
    private BigDecimal longitude;
}
