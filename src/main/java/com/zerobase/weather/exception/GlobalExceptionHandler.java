package com.zerobase.weather.exception;

import com.zerobase.weather.dto.ApiResponse;
import com.zerobase.weather.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.zerobase.weather.type.ErrorCode.INVALID_REQUEST;
import static com.zerobase.weather.type.ErrorCode.TYPE_MISS_MATCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                BAD_REQUEST,
                INVALID_REQUEST,
                null,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Object> typeMissMatchException(MethodArgumentTypeMismatchException e) {
        return ApiResponse.of(
                BAD_REQUEST,
                TYPE_MISS_MATCH,
                null,
                null
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ArgumentException.class)
    public ApiResponse<Object> argumentException(ArgumentException e) {
        return ApiResponse.of(
                BAD_REQUEST,
                e.getErrorCode(),
                null,
                null
        );
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> exception(Exception e) {
        log.error("Exception : ", e);
        return ApiResponse.of(
                INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR,
                null,
                null
        );
    }

}
