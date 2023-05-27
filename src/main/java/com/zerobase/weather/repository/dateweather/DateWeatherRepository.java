package com.zerobase.weather.repository.dateweather;

import com.zerobase.weather.domain.dateweather.DateWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {
    Optional<DateWeather> findByDate(LocalDate localDate);
}
