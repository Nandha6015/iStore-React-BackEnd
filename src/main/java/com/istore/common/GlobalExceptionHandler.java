package com.istore.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleException(Exception exception) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiResponse.setError("Internal Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException exception) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        apiResponse.setError(exception.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException exception) {

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(HttpStatus.NOT_FOUND.value());
        apiResponse.setError(exception.getErrors());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
