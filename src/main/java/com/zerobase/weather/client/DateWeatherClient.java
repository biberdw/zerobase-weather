package com.zerobase.weather.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.dto.dateweather.WeatherApiDto;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DateWeatherClient {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=37.5666791&lon=126.9782914&appid=";
    @Value("${openweathermap.key}")
    private String API_KEY;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public DateWeather getDataFromApi(LocalDate localDate) {
        String url = API_URL + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String json = Objects.requireNonNull(response.body()).string();
            WeatherApiDto dto = objectMapper.readValue(json, WeatherApiDto.class);
            return dto.toEntity(localDate);
        } catch (IOException e) {
            throw new RuntimeException(e); //request 실패
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }

    }
}
