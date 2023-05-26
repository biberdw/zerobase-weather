package com.zerobase.weather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    TYPE_MISS_MATCH("타입이 일치하지 않습니다."),
    FUTURE_DATE_NOT_ALLOWED("미래 일기는 현재 지원되지 않습니다.");

    private final String description;
}
