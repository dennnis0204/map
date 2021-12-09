package com.desiredchargingstation.map.controller;

import com.desiredchargingstation.map.dto.ChargingPointDto;
import com.desiredchargingstation.map.dto.NewChargingPointDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithUserDetails(value = "john@gmail.com")
@Sql(value = {"/create-user-before.sql", "/create-points-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-points-after.sql", "/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class ChargingPointRestControllerIntegrationTest {

    private static final String USER_MAX_POINTS_NUMBER_EXCEEDED_MESSAGE = "User already has %s charging points on the map!";
    private static final String USER_HAS_NO_SUCH_POINT_MESSAGE = "User has no such point!";

    @Value("${settings.user.maxPointsNumber}")
    private Integer userMaxPointsNumber;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static NewChargingPointDto validNewChargingPoint;
    private static ChargingPointDto validChargingPointDto;
    private static ChargingPointDto invalidChargingPointDto;
    private static ChargingPointDto persistedChargingPointDto;
    private static ChargingPointDto nonexistentChargingPointDto;
    private static ChargingPointDto updatedChargingPointDto;

    @BeforeAll
    public static void init() {
        validNewChargingPoint = new NewChargingPointDto();
        validNewChargingPoint.setLongitude(new BigDecimal("22.2222"));
        validNewChargingPoint.setLatitude(new BigDecimal("11.1111"));

        validChargingPointDto = new ChargingPointDto();
        validChargingPointDto.setId(7L);
        validChargingPointDto.setLongitude(new BigDecimal("22.2222"));
        validChargingPointDto.setLatitude(new BigDecimal("11.1111"));

        invalidChargingPointDto = new ChargingPointDto();
        invalidChargingPointDto.setId(-7L);
        invalidChargingPointDto.setLongitude(new BigDecimal("99.22229"));
        invalidChargingPointDto.setLatitude(new BigDecimal("-99.3333"));

        persistedChargingPointDto = new ChargingPointDto();
        persistedChargingPointDto.setId(6L);
        persistedChargingPointDto.setLatitude(new BigDecimal("51.1313"));
        persistedChargingPointDto.setLongitude(new BigDecimal("33.6666"));

        nonexistentChargingPointDto = new ChargingPointDto();
        nonexistentChargingPointDto.setId(88L);
        nonexistentChargingPointDto.setLatitude(new BigDecimal("51.1313"));
        nonexistentChargingPointDto.setLongitude(new BigDecimal("33.6666"));

        updatedChargingPointDto = new ChargingPointDto();
        updatedChargingPointDto.setId(6L);
        updatedChargingPointDto.setLatitude(new BigDecimal("31.4525"));
        updatedChargingPointDto.setLongitude(new BigDecimal("54.4523"));
    }

    @Test
    @Sql(value = {"/delete-one-point.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenValidJsonPoint_whenAddNewPointAndNumberOfPointsNotExceeded_thenReturnUserWithPoints() throws Exception {
        mockMvc.perform(post("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validNewChargingPoint)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chargingPoints[0].latitude", is(11.1111)))
                .andExpect(jsonPath("$.chargingPoints[0].longitude", is(22.2222)));
    }

    @Test
    @Sql(value = {"/delete-one-point.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void givenInvalidJsonPoint_whenAddNewPoint_thenReturnErrorResponse() throws Exception {
        mockMvc.perform(post("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidChargingPointDto)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors[*].field", Matchers.containsInAnyOrder(
                        "latitude", "longitude", "longitude")))
                .andExpect(jsonPath("$.errors[*].message", Matchers.containsInAnyOrder(
                        "Max longitude value is 180.00",
                        "Min latitude value is -90.00",
                        "Wrong longitude format")));
    }

    @Test
    public void givenValidJsonPoint_whenAddNewPointAndUserPointsNumberExceeded_thenReturnErrorResponse() throws Exception {
        mockMvc.perform(post("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validNewChargingPoint)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(String.format(USER_MAX_POINTS_NUMBER_EXCEEDED_MESSAGE, userMaxPointsNumber))));
    }

    @Test
    public void givenValidJsonPoint_whenDeletePointAndUserHasPointWithSameId_thenReturnUserWithPoints() throws Exception {
        mockMvc.perform(delete("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(persistedChargingPointDto)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chargingPoints.length()").value(1));
    }

    @Test
    public void givenValidJsonPointWithNonexistentId_whenDeletePoint_thenReturnErrorResponse() throws Exception {
        mockMvc.perform(delete("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nonexistentChargingPointDto)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(USER_HAS_NO_SUCH_POINT_MESSAGE)));
    }

    @Test
    public void givenValidJsonPoint_whenUpdatePoint_thenReturnUserWithPoints() throws Exception {
        mockMvc.perform(put("/points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedChargingPointDto)))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chargingPoints[1].id", is(6)))
                .andExpect(jsonPath("$.chargingPoints[1].latitude", is(31.4525)))
                .andExpect(jsonPath("$.chargingPoints[1].longitude", is(54.4523)));
    }
}
