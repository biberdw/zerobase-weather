package com.zerobase.weather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    TYPE_MISS_MATCH("타입이 일치하지 않습니다."),
    FUTURE_DATE_NOT_ALLOWED("미래 일기는 현재 지원되지 않습니다."),
    INVALID_DATE_RANGE("시작날짜가 종료날짜보다 미래일 수 없습니다")
    ;

    private final String description;
}
