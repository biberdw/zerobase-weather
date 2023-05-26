package com.zerobase.weather.service.diary;

import com.zerobase.weather.domain.dateweather.DateWeather;
import com.zerobase.weather.domain.diary.Diary;
import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.exception.ArgumentException;
import com.zerobase.weather.repository.diary.DiaryRepository;
import com.zerobase.weather.service.dateweather.DateWeatherDbOrApiFetcher;
import com.zerobase.weather.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.weather.type.ErrorCode.*;
import static com.zerobase.weather.type.ErrorCode.FUTURE_DATE_NOT_ALLOWED;
import static com.zerobase.weather.type.ErrorCode.INVALID_DATE_RANGE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DateWeatherDbOrApiFetcher fetcher;
    private final DiaryRepository diaryRepository;

    @Transactional
    public DiaryDto createDiary(LocalDate date, String text) {
        //미래의 일기를 저장하려고 할때 예외 반환
        throwIfFutureDateBy(date);

        //날씨 데이터 가져오기 (API 에서 가져오기 or DB 에서 가져오기 )
        DateWeather dateWeather = fetcher.fetch(date);

        //가져온 날씨데이터와 요청받은 일기내용 및 날짜로 일기 생성
        Diary diary = createEntityBy(date, text, dateWeather);

        //일기저장
        Diary savedDiary = diaryRepository.save(diary);

        return DiaryDto.fromEntity(savedDiary);
    }


    public List<DiaryDto> readDiaries(LocalDate localDate) {
        //미래의 일기를 조회하려 할때 예외 발생
        throwIfFutureDateBy(localDate);

        return diaryRepository.findAllByDate(localDate)
                .stream().map(DiaryDto::fromEntity)
                .collect(Collectors.toList());
    }


    public List<DiaryDto> readDiariesBetween(LocalDate startDate, LocalDate endDate) {
        //미래의 일기를 조회하려 할때 예외 발생
        throwIfFutureDateBy(startDate);

        //시작값이 종료값보다 미래면은 예외 발생
        throwIfInvalidDateRange(startDate, endDate);

        return diaryRepository.findAllByDateBetween(startDate, endDate)
                .stream().map(DiaryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaryDto updateOldestTextBy(LocalDate localDate, String text) {
        Diary diary = diaryRepository.getFirstByDate(localDate)
                .orElseThrow(() -> new ArgumentException(DIARY_DOES_NOT_EXIST));
        diary.updateText(text);
        return DiaryDto.fromEntity(diary);
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

    private static void throwIfFutureDateBy(LocalDate date) {
        if (date.isAfter(LocalDate.now())) throw new ArgumentException(FUTURE_DATE_NOT_ALLOWED);
    }

    private static void throwIfInvalidDateRange(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)) throw new ArgumentException(INVALID_DATE_RANGE);
    }
}
