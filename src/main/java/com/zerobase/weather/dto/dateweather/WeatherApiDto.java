package com.zerobase.weather.dto.dateweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.weather.domain.dateweather.DateWeather;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class WeatherApiDto {


    private LocalDate date;
    @JsonProperty("weather")
    private List<WeatherData> weatherData;
    @JsonProperty("main")
    private TempData tempData;


    public DateWeather toEntity(LocalDate localDate) {
        return DateWeather
                .builder()
                .date(localDate)
                .weather(this.weatherData.get(0).weather)
                .icon(this.weatherData.get(0).icon)
                .temperature(this.tempData.temperature)
                .build();
    }

    @NoArgsConstructor
    @Getter
    static class WeatherData {
        @JsonProperty("main")
        private String weather;
        @JsonProperty("icon")
        private String icon;
    }

    @NoArgsConstructor
    @Getter
    static class TempData {
        @JsonProperty("temp")
        private Double temperature;
    }
}
