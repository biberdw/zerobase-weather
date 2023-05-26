package com.zerobase.weather.dto;

import com.zerobase.weather.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private int code;
    private HttpStatus status;
    private ErrorCode errorCode;
    private String errorMessage;
    private T data;

    private ApiResponse(HttpStatus status, ErrorCode errorCode, T data, String message) {
        this.code = status.value();
        this.status = status;
        this.errorCode = errorCode;

        if (errorCode != null && message != null) this.errorMessage = message;
        else if (errorCode == null && message == null) this.errorMessage = null;
        else this.errorMessage = errorCode.getDescription();

        this.data = data;
    }

    /**
     * code : {status Code}<br>
     * status : {statusName}<br>
     * errorCode : {errorCode}<br>
     * errorMessage : {errorMessage}<br>
     * data : null
     */
    public static <T> ApiResponse<T> of(HttpStatus status, ErrorCode errorCode, T data, String message) {
        return new ApiResponse<>(status, errorCode, data, message);
    }

    /**
     * code : {code}<br>
     * status : {statusName}<br>
     * errorCode : null<br>
     * errorMessage : null<br>
     * data : {data}
     */
    public static <T> ApiResponse<T> of(HttpStatus status, T data, String message) {
        return of(status, null, data, message);
    }

    /**
     * code : 200<br>
     * status : OK<br>
     * errorCode : null<br>
     * errorMessage : null<br>
     * data : {data}
     */
    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data, null);
    }

    /**
     * code : 200<br>
     * status : OK<br>
     * errorCode : nul<br>
     * errorMessage : null<br>
     * data : null<br>
     */
    public static <T> ApiResponse<T> ok() {
        return ok(null);
    }
}
