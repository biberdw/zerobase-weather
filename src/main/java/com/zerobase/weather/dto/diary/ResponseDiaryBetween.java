package com.zerobase.weather.dto.diary;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ResponseDiaryBetween {

    private String weather;
    private String icon;
    private Double temperature;
    private String text;
    private LocalDate date;

    @Builder
    private ResponseDiaryBetween(String weather, String icon, Double temperature, String text, LocalDate date) {
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
        this.text = text;
        this.date = date;
    }

    public static ResponseDiaryBetween of(DiaryDto dto) {
        return ResponseDiaryBetween.builder()
                .weather(dto.getWeather())
                .icon(dto.getIcon())
                .temperature(dto.getTemperature())
                .text(dto.getText())
                .date(dto.getDate())
                .build();
    }
}
