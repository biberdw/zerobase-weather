package com.zerobase.weather.service.diary;

import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.service.diary.DiaryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

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
        LocalDate localDate = LocalDate.of(2022,05,23);

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

}
