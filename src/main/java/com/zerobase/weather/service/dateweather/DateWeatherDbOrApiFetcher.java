package com.zerobase.weather.service.dateweather;

import com.zerobase.weather.client.DateWeatherClient;
import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.repository.dateweather.DateWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class DateWeatherDbOrApiFetcher {

    private final DateWeatherClient dateWeatherClient;
    private final DateWeatherRepository dateWeatherRepository;

    public DateWeather fetch(LocalDate date){
        return dateWeatherRepository.findByDate(date).orElseGet(() -> dateWeatherClient.getDataFromApi(date));
    }
}
