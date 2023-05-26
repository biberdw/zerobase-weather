package com.zerobase.weather.exception;

import com.zerobase.weather.type.ErrorCode;
import lombok.Getter;

@Getter
public class ArgumentException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public ArgumentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
