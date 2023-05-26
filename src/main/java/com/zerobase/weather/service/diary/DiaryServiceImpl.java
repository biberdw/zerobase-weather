package com.zerobase.weather.service.diary;

import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.domain.diary.Diary;
import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.exception.ArgumentException;
import com.zerobase.weather.repository.diary.DiaryRepository;
import com.zerobase.weather.service.dateweather.DateWeatherDbOrApiFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.zerobase.weather.type.ErrorCode.FUTURE_DATE_NOT_ALLOWED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DateWeatherDbOrApiFetcher fetcher;
    private final DiaryRepository diaryRepository;

    @Transactional
    public DiaryDto createDiary(LocalDate date, String text) {
        //미래의 일기를 저장하려고 할때 예외 반환
        if (date.isAfter(LocalDate.now())) throw new ArgumentException(FUTURE_DATE_NOT_ALLOWED);

        //날씨 데이터 가져오기 (API 에서 가져오기 or DB 에서 가져오기 )
        DateWeather dateWeather = fetcher.fetch(date);

        //가져온 날씨데이터와 요청받은 일기내용 및 날짜로 일기 생성
        Diary diary = createEntityBy(date, text, dateWeather);

        //일기저장
        Diary savedDiary = diaryRepository.save(diary);

        return DiaryDto.fromEntity(savedDiary);
    }

    private Diary createEntityBy(LocalDate date, String text, DateWeather dateWeather) {
        return Diary
                .builder()
                .date(date)
                .icon(dateWeather.getIcon())
                .weather(dateWeather.getWeather())
                .temperature(dateWeather.getTemperature())
                .text(text)
                .build();
    }
}
