package com.desiredchargingstation.map.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    public static final String VALIDATION_FAILED_MESSAGE = "Validation failed";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(VALIDATION_FAILED_MESSAGE);
        List<ErrorResponse.FieldMessage> fieldMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    ErrorResponse.FieldMessage fieldMessage = new ErrorResponse.FieldMessage();
                    fieldMessage.setField(fieldError.getField());
                    fieldMessage.setMessage(fieldError.getDefaultMessage());
                    return fieldMessage;
                })
                .collect(Collectors.toList());
        errorResponse.setErrors(fieldMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(VALIDATION_FAILED_MESSAGE);
        List<ErrorResponse.FieldMessage> fieldMessages = ex.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    ErrorResponse.FieldMessage fieldMessage = new ErrorResponse.FieldMessage();
                    final Iterator<Path.Node> iterator = constraintViolation.getPropertyPath().iterator();
                    String fieldName = null;
                    while (iterator.hasNext()) {
                        fieldName = iterator.next().getName();
                    }
                    fieldMessage.setField(fieldName);
                    fieldMessage.setMessage(constraintViolation.getMessage());
                    return fieldMessage;
                })
                .collect(Collectors.toList());
        errorResponse.setErrors(fieldMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserMaxPointsNumberExceededException.class)
    public ResponseEntity<ErrorResponse> handleUserMaxPointsNumberExceeded(UserMaxPointsNumberExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChargingPointDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleChargingPointDoesNotExist(ChargingPointDoesNotExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
