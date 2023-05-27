package com.zerobase.weather.dto.diary;

import com.zerobase.weather.domain.diary.Diary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiaryDto {

    private Long id;
    private String weather;
    private String icon;
    private Double temperature;
    private String text;
    private LocalDate date;

    @Builder
    private DiaryDto(Long id, String weather, String icon, Double temperature, String text, LocalDate date) {
        this.id = id;
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
        this.text = text;
        this.date = date;
    }

    public static DiaryDto fromEntity(Diary diary) {
        return DiaryDto.builder()
                .id(diary.getId())
                .weather(diary.getWeather())
                .icon(diary.getIcon())
                .temperature(diary.getTemperature())
                .text(diary.getText())
                .date(diary.getDate())
                .build();
    }
}
