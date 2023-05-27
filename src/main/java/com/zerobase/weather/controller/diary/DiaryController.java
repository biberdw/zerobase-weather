package com.zerobase.weather.controller.diary;

import com.zerobase.weather.dto.ApiResponse;
import com.zerobase.weather.dto.diary.*;
import com.zerobase.weather.service.diary.DiaryService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장")
    @PostMapping("/{date}")
    public ApiResponse<Void> createDiary(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                         @RequestBody String text) {
        diaryService.createDiary(date, text);
        return ApiResponse.ok();
    }

    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/{date}")
    public ApiResponse<List<ResponseDiary>> readDiaries(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.ok(
                diaryService.readDiaries(date)
                        .stream().map(ResponseDiary::of)
                        .collect(Collectors.toList())
        );

    }

    @ApiOperation("선택한 기간중의 모든 일기 데이터를 가져옵니다")
    @GetMapping
    public ApiResponse<List<ResponseDiaryBetween>> readDiariesBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ApiResponse.ok(
                diaryService.readDiariesBetween(startDate, endDate)
                        .stream().map(ResponseDiaryBetween::of)
                        .collect(Collectors.toList())
        );

    }

    @ApiOperation("해당 날짜의 첫번째 일기 글을 수정합니다")
    @PutMapping("/oldest/{date}")
    public ApiResponse<UpdateOldestDto.Response> updateOldestDiary(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @Valid @RequestBody UpdateOldestDto.Request request){

        return ApiResponse.ok(
                UpdateOldestDto.Response.of(
                        diaryService.updateOldestTextBy(date, request.getText())
                )
        );
    }

    @ApiOperation("해당 날짜의 모든 일기를 지웁니다")
    @DeleteMapping("/{date}")
    public ApiResponse<DeleteDiaries.Response> deleteDiaries(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ApiResponse.ok(
                DeleteDiaries.Response.of(
                diaryService.deleteDiariesBy(date)
                )
        );
    }



}
