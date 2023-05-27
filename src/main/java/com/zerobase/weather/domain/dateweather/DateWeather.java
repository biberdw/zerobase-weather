package com.zerobase.weather.domain.dateweather;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DateWeather {
    @Id
    private LocalDate date;
    @NotNull
    private String weather;
    @NotNull
    private String icon;
    @NotNull
    private Double temperature;

    @Builder
    private DateWeather(LocalDate date, String weather, String icon, Double temperature) {
        this.date = date;
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
    }
}
