package com.zerobase.weather.repository.dateweather;

import com.zerobase.weather.domain.dateweather.DateWeather;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DateWeatherRepositoryTest {

    @Autowired
    DateWeatherRepository dateWeatherRepository;

    @Test
    @DisplayName("DB 에 날씨데이터가 존재하면 해당 날짜와 같은 날씨데이터가 조회돼야 한다")
    public void findByDate() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        DateWeather dateWeather = DateWeather.builder()
                .date(localDate)
                .icon("icon")
                .weather("맑음")
                .temperature(222.0)
                .build();
        dateWeatherRepository.save(dateWeather);

        //when
        DateWeather findDateWeather = dateWeatherRepository.findByDate(localDate).orElseThrow(() -> new IllegalArgumentException("없는 데이터"));

        //then
        assertThat(findDateWeather)
                .extracting("icon", "date", "weather", "temperature")
                .contains(localDate, "icon", localDate, "맑음", 222.0);

    }

}