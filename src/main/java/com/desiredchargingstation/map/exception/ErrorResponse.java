package com.desiredchargingstation.map.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {

    private String timestamp;
    int status;
    private String error;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FieldMessage> errors;

    public ErrorResponse(HttpStatus httpStatus) {
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.errors = new ArrayList<>();
    }

    @Data
    public static class FieldMessage {
        private String field;
        private String message;
    }
}
