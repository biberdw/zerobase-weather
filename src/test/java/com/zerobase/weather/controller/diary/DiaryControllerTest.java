package com.zerobase.weather.controller.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.weather.service.diary.DiaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static com.zerobase.weather.type.ErrorCode.FUTURE_DATE_NOT_ALLOWED;
import static com.zerobase.weather.type.ErrorCode.TYPE_MISS_MATCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.errorCode").isEmpty())
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("새 일기를 등록할 때 date 형식을 맞추지 않을 경우 error 가 발생한다")
    public void typeMissMatch() throws Exception {
        //given
        String json = "와 재밌다";

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/diary/{date}", "05-20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(TYPE_MISS_MATCH.name()))
                .andExpect(jsonPath("$.errorMessage").value(TYPE_MISS_MATCH.getDescription()))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @DisplayName("미래의 일기를 등록할 때 에러가 발생한다 ")
    public void futureDateNotAllowed() throws Exception {
        //given
        String json = "와 재밌다";
        LocalDate localDate = LocalDate.now().plusDays(1);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/diary/{date}", localDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.name()))
                .andExpect(jsonPath("$.errorCode").value(FUTURE_DATE_NOT_ALLOWED.name()))
                .andExpect(jsonPath("$.errorMessage").value(FUTURE_DATE_NOT_ALLOWED.getDescription()))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}