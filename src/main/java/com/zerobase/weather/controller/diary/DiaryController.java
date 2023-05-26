package com.zerobase.weather.controller.diary;

import com.zerobase.weather.dto.ApiResponse;
import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.dto.diary.ResponseDiary;
import com.zerobase.weather.dto.diary.ResponseDiaryBetween;
import com.zerobase.weather.dto.diary.UpdateOldestDto;
import com.zerobase.weather.service.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping
    public ApiResponse<List<ResponseDiaryBetween>> readDiariesBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.ok(
                diaryService.readDiariesBetween(startDate, endDate)
                        .stream().map(ResponseDiaryBetween::of)
                        .collect(Collectors.toList())
        );

    }

    @PutMapping("/oldest/{date}")
    public ApiResponse<UpdateOldestDto.Response> updateOldestDiary(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @Valid @RequestBody UpdateOldestDto.Request request){

        return ApiResponse.ok(
                UpdateOldestDto.Response.of(
                        diaryService.updateOldestTextBy(date, request.getText())
                )
        );
    }



}
