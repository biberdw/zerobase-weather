package com.zerobase.weather.scheduler;


import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.repository.dateweather.DateWeatherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "schedules.cron.dateweather=0/5 * * * * ?")
@Transactional
public class WeatherSchedulerTest {

    @Autowired
    private DateWeatherRepository weatherRepository;

    @Test
    @DisplayName("정해진 시간에 api 로 가져온 데이터가 저장되어 있어야 한다")
    public void schedulerSuccess() throws InterruptedException {

        Thread.sleep(8000); //5초에 날씨데이터를 가져오기때문에 8초까지 기다림
        List<DateWeather> dateWeathers = weatherRepository.findAll();

        assertThat(dateWeathers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("정해진 시간이 되지 않으면 api 로 가져온 데이터가 없어야 한다")
    public void schedulerSuccess2() {

        List<DateWeather> dateWeathers = weatherRepository.findAll();

        assertThat(dateWeathers.size()).isEqualTo(0);
    }

}
