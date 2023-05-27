package com.zerobase.weather.controller.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.dto.diary.UpdateOldestDto;
import com.zerobase.weather.service.diary.DiaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.zerobase.weather.type.ErrorCode.INVALID_REQUEST;
import static com.zerobase.weather.type.ErrorCode.TYPE_MISS_MATCH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DiaryController.class)
class DiaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiaryService diaryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("새 일기를 등록한다")
    public void createDiary() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        String json = "와 재밌다";


        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/diary/{date}", localDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.errorCode").isEmpty())
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("새 일기를 등록할 때 date 형식을 맞추지 않을 경우 error 가 발생한다")
    public void createDiary_typeMissMatch() throws Exception {
        //given
        String json = "와 재밌다";

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/diary/{date}", "05-20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("해당 날짜의 일기들을 조회한다")
    public void readDiaries() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);

        DiaryDto diary1 = createDiaryDtoBy(localDate, "맑음", "icon1", 222.1, "와1");
        DiaryDto diary2 = createDiaryDtoBy(localDate, "흐림", "icon2", 222.2, "와2");
        DiaryDto diary3 = createDiaryDtoBy(localDate, "좋음", "icon3", 222.3, "와3");

        List<DiaryDto> diaries = Arrays.asList(diary1, diary2, diary3);

        given(diaryService.readDiaries(any()))
                .willReturn(diaries);

        //when //then
        mockMvc.perform(get(("/diary/{date}"), localDate)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.errorCode").isEmpty())
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].weather").value("맑음"))
                .andExpect(jsonPath("$.data[0].icon").value("icon1"))
                .andExpect(jsonPath("$.data[0].temperature").value(222.1))
                .andExpect(jsonPath("$.data[0].text").value("와1"));

    }

    @Test
    @DisplayName("일기들을 조회할때 date 형식을 맞추지 않을 경우 error 가 발생한다")
    public void readDiaries_typeMissMatch() throws Exception {
        //given
        String targetDate = "05-20";

        //when //then
        mockMvc.perform(get(("/diary/{date}"), targetDate)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("시작값과 종료일안에 포함되는 일기들을 조회한다")
    public void readDiaries_between() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2022, 05, 23);
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        LocalDate endDate = LocalDate.of(2022, 05, 23);
        String json = "와 재밌다";

        DiaryDto diary1 = createDiaryDtoBy(startDate, "맑음", "icon1", 222.1, "와1");
        DiaryDto diary2 = createDiaryDtoBy(localDate, "흐림", "icon2", 222.2, "와2");
        DiaryDto diary3 = createDiaryDtoBy(endDate, "좋음", "icon3", 222.3, "와3");

        List<DiaryDto> diaries = Arrays.asList(diary1, diary2, diary3);

        given(diaryService.readDiariesBetween(any(), any()))
                .willReturn(diaries);

        //when //then
        mockMvc.perform(get(("/diary"))
                        .queryParam("startDate", "2022-05-23")
                        .queryParam("endDate", "2022-05-25")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.errorCode").isEmpty())
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].weather").value("맑음"))
                .andExpect(jsonPath("$.data[0].icon").value("icon1"))
                .andExpect(jsonPath("$.data[0].temperature").value(222.1))
                .andExpect(jsonPath("$.data[0].text").value("와1"))
                .andExpect(jsonPath("$.data[0].date").value("2022-05-23"));

    }


    @DisplayName("일기들을 날짜범위로 조회할때 date 형식을 맞추지 않을 경우 error 가 발생한다")
    @TestFactory
    public Collection<DynamicTest> readDiaries_between_typeMissMatch() throws Exception {

        return Arrays.asList(
                DynamicTest.dynamicTest("시작날짜의 포맷이 일치하지 않은 경우 예외 발생", () -> {
                    mockMvc.perform(get(("/diary"))
                                    .queryParam("startDate", "05-23")
                                    .queryParam("endDate", "2022-05-25")
                            )
                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                            .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                            .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                            .andExpect(jsonPath("$.data").isEmpty());
                }),
                DynamicTest.dynamicTest("종료날짜의 포맷이 일치하지 않은 경우 예외 발생", () -> {
                    mockMvc.perform(get(("/diary"))
                                    .queryParam("startDate", "2022-05-23")
                                    .queryParam("endDate", "05-25")
                            )
                            .andDo(print())
                            .andExpect(status().isBadRequest())
                            .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                            .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                            .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                            .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                            .andExpect(jsonPath("$.data").isEmpty());
                })

        );

    }

    @Test
    @DisplayName("해당 날짜의 첫번째 일기 글을 새로 받아온 일기글로 수정한다")
    public void updateOldestDiary() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        String text = "와재밌다";

        UpdateOldestDto.Request request = UpdateOldestDto.Request.builder()
                .text(text)
                .build();

        String json = objectMapper.writeValueAsString(request);

        DiaryDto diary1 = createDiaryDtoBy(localDate, "맑음", "icon1", 222.1, text);

        given(diaryService.updateOldestTextBy(any(), any()))
                .willReturn(diary1);

        //when //then
        mockMvc.perform(put(("/diary/oldest/{date}"), localDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.errorCode").isEmpty())
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.data.weather").value("맑음"))
                .andExpect(jsonPath("$.data.icon").value("icon1"))
                .andExpect(jsonPath("$.data.temperature").value(222.1))
                .andExpect(jsonPath("$.data.text").value(text))
                .andExpect(jsonPath("$.data.date").value(localDate.toString()));

    }

    @Test
    @DisplayName("해당 날짜의 첫번째 일기 글을 새로 받아온 일기글로 수정할 때 date 형식을 맞추지 않을 경우 error 가 발생한다")
    public void updateOldestDiary_typeMissMatch() throws Exception {
        //given
        String targetDate = "05-20";
        String text = "와재밌다";

        UpdateOldestDto.Request request = UpdateOldestDto.Request.builder()
                .text(text)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(put(("/diary/oldest/{date}"), targetDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("해당 날짜의 첫번째 일기 글을 새로 받아온 일기글로 수정할 때 text 필드가 null 인 경우 error 가 발생한다")
    public void updateOldestDiary_textNull() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);

        UpdateOldestDto.Request request = UpdateOldestDto.Request.builder()
                .text(null)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(put(("/diary/oldest/{date}"), localDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(INVALID_REQUEST.name()))
                .andExpect(jsonPath("$.errorMessage").value("text 필드는 null이 허용되지 않습니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("해당 날짜의 모든 일기를 지운다")
    public void deleteDiaries() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);

        given(diaryService.deleteDiariesBy(any()))
                .willReturn(4);

        //when //then
        mockMvc.perform(delete(("/diary/{date}"), localDate)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.errorCode").isEmpty())
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.data.result").value("004개의 일기가 삭제 되었습니다."));

    }

    @Test
    @DisplayName("해당 날짜의 모든 일기를 지울 때 date 형식을 맞추지 않을 경우 error 가 발생한다")
    public void deleteDiaries_typeMissMatch() throws Exception {
        //given
        String targetDate = "05-20";

        //when //then
        mockMvc.perform(delete(("/diary/{date}"), targetDate)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    private static DiaryDto createDiaryDtoBy(LocalDate localDate, String weather, String icon, double temperature, String text) {
        return DiaryDto.builder()
                .weather(weather)
                .icon(icon)
                .temperature(temperature)
                .text(text)
                .date(localDate)
                .build();
    }

}