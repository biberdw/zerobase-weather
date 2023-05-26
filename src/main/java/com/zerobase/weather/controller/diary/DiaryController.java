package com.zerobase.weather.controller.diary;

import com.zerobase.weather.dto.ApiResponse;
import com.zerobase.weather.exception.ArgumentException;
import com.zerobase.weather.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.zerobase.weather.type.ErrorCode.FUTURE_DATE_NOT_ALLOWED;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;


    @PostMapping("/{date}")
    public ApiResponse<Void> createDiary(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                         @RequestBody String text) {
        diaryService.createDiary(date, text);
        return ApiResponse.ok();
    }
}
