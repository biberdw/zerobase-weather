package com.zerobase.weather.dto.diary;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UpdateOldestDto {

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Request{
        @NotNull(message = "text 필드는 null이 허용되지 않습니다.")
        private String text;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private String weather;
        private String icon;
        private Double temperature;
        private String text;
        private LocalDate date;

        @Builder
        private Response(String weather, String icon, Double temperature, String text, LocalDate date) {
            this.weather = weather;
            this.icon = icon;
            this.temperature = temperature;
            this.text = text;
            this.date = date;
        }

        public static Response of(DiaryDto diaryDto){
            return Response.builder()
                    .weather(diaryDto.getWeather())
                    .icon(diaryDto.getIcon())
                    .temperature(diaryDto.getTemperature())
                    .text(diaryDto.getText())
                    .date(diaryDto.getDate())
                    .build();
        }
    }
}
