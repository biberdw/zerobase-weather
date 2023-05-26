package com.zerobase.weather.exception;

import com.zerobase.weather.dto.ApiResponse;
import com.zerobase.weather.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BindException.class)
//    public ApiResponse<Object> bindException(BindException e) {
//        return ApiResponse.of(
//                HttpStatus.BAD_REQUEST,
//                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
//        );
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Object> typeMissMatchException(MethodArgumentTypeMismatchException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                ErrorCode.TYPE_MISS_MATCH,
                null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ArgumentException.class)
    public ApiResponse<Object> argumentException(ArgumentException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getErrorCode(),
                null
        );
    }

}
