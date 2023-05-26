package com.zerobase.weather.service.diary;

import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.exception.ArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.zerobase.weather.type.ErrorCode.FUTURE_DATE_NOT_ALLOWED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DiaryServiceTest {

    @Autowired
    DiaryService diaryService;

    @Test
    @DisplayName("날씨데이터를 조회 해 입력받은 일기데이터로 일기를 저장해야 한다")
    public void createDiary() throws Exception {
        //given
        String text = "와";
        LocalDate localDate = LocalDate.of(2022, 05, 23);

        //when
        DiaryDto diaryDto = diaryService.createDiary(localDate, text);

        //then
        assertThat(diaryDto.getId()).isNotNull();
        assertThat(diaryDto)
                .extracting("text", "date")
                .contains(localDate, text);
        assertThat(diaryDto.getWeather()).isNotNull();
        assertThat(diaryDto.getIcon()).isNotNull();
        assertThat(diaryDto.getTemperature()).isNotNull();

    }

    @Test
    @DisplayName("미래의 일기를 등록할 때 예외가 발생한다 ")
    public void futureDateNotAllowed() throws Exception {
        //given
        String text = "와";
        LocalDate localDate = LocalDate.now().plusDays(1);

        //when
        ArgumentException exception = assertThrows(ArgumentException.class, () -> diaryService.createDiary(localDate, text));

        assertThat(exception)
                .extracting("errorCode", "errorMessage")
                .contains(FUTURE_DATE_NOT_ALLOWED, FUTURE_DATE_NOT_ALLOWED.getDescription());


    }

}
