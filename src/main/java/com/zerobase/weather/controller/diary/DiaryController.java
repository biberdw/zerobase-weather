package com.zerobase.weather.controller.diary;

import com.zerobase.weather.dto.ApiResponse;
import com.zerobase.weather.dto.diary.ResponseDiary;
import com.zerobase.weather.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/{date}")
    public ApiResponse<List<ResponseDiary>> readDiaries(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.ok(
                diaryService.readDiaries(date)
                        .stream().map(ResponseDiary::of)
                        .collect(Collectors.toList())
        );

    }


}
