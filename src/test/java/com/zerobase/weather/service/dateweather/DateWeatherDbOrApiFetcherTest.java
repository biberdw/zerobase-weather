package com.zerobase.weather.service.dateweather;

import com.zerobase.weather.client.DateWeatherClient;
import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.repository.dateweather.DateWeatherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
class DateWeatherDbOrApiFetcherTest {

    @MockBean
    DateWeatherClient dateWeatherClient;

    @Autowired
    DateWeatherDbOrApiFetcher fetcher;

    @Autowired
    DateWeatherRepository dateWeatherRepository;

    @Test
    @DisplayName("DB 에 데이터가 존재하지 않으면 API 에서 가져와야 한다")
    public void fetchFromApi() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        given(dateWeatherClient.getDataFromApi(any()))
                .willReturn(DateWeather.builder()
                        .date(localDate)
                        .icon("icon")
                        .weather("맑음")
                        .temperature(222.0)
                        .build());

        //when
        DateWeather dateWeatherFromApi = fetcher.fetch(localDate);

        //then
        verify(dateWeatherClient, times(1)).getDataFromApi(any());
        assertThat(dateWeatherFromApi)
                .extracting("icon", "date", "weather", "temperature")
                .contains(localDate, "icon", localDate, "맑음", 222.0);
    }

    @Test
    @DisplayName("DB 에 데이터가 존재하면 API 가 아닌 DB 에서 조회해야 한다")
    public void fetchFromDB() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        DateWeather dateWeather = DateWeather.builder()
                .date(localDate)
                .icon("icon")
                .weather("맑음")
                .temperature(222.0)
                .build();

        DateWeather savedDateWeather = dateWeatherRepository.save(dateWeather);

        //when
        DateWeather dateWeatherFromDB = fetcher.fetch(localDate);

        //then
        verify(dateWeatherClient, times(0)).getDataFromApi(any());
        assertThat(dateWeatherFromDB)
                .extracting("icon", "date", "weather", "temperature")
                .contains(savedDateWeather.getIcon(), savedDateWeather.getDate(), savedDateWeather.getWeather(), savedDateWeather.getTemperature());
    }

}