package com.zerobase.weather.service.diary;

import com.zerobase.weather.dto.diary.DiaryDto;

import java.time.LocalDate;
import java.util.List;

public interface DiaryService {

    DiaryDto createDiary(LocalDate date, String text);

    List<DiaryDto> readDiaries(LocalDate localDate);

    List<DiaryDto> readDiariesBetween(LocalDate startDate, LocalDate endDate);

    DiaryDto updateOldestTextBy(LocalDate localDate, String text);

    int deleteDiariesBy(LocalDate localDate);
}
