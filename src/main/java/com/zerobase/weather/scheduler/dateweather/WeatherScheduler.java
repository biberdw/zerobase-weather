package com.zerobase.weather.scheduler.dateweather;


import com.zerobase.weather.client.DateWeatherClient;
import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.repository.dateweather.DateWeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WeatherScheduler {

    private final DateWeatherClient dateWeatherClient;
    private final DateWeatherRepository dateWeatherRepository;

    @Scheduled(cron = "${schedules.cron.dateweather}")
    public void saveWeatherData() {

        DateWeather dateWeather = dateWeatherClient.getDataFromApi(LocalDate.now());
        log.info("날씨데이터를 저장하였습니다. {}", LocalDateTime.now());
        //날씨데이터 DB 에 저장하기
        dateWeatherRepository.save(dateWeather);
    }
}