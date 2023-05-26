package com.zerobase.weather.service.diary;

import com.zerobase.weather.dto.diary.DiaryDto;

import java.time.LocalDate;

public interface DiaryService {

    DiaryDto createDiary(LocalDate date, String text);
}
