package com.zerobase.weather.service.diary;

import com.zerobase.weather.config.AcceptanceTest;
import com.zerobase.weather.domain.diary.Diary;
import com.zerobase.weather.dto.diary.DiaryDto;
import com.zerobase.weather.exception.ArgumentException;
import com.zerobase.weather.repository.diary.DiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.zerobase.weather.type.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AcceptanceTest
public class DiaryServiceTest {

    @Autowired
    private DiaryService diaryService;
    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    @DisplayName("날씨데이터를 조회 해 입력받은 일기데이터로 일기를 저장해야 한다")
    public void createDiaryBy() throws Exception {
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
    public void create_futureDateNotAllowed() throws Exception {
        //given
        String text = "와";
        LocalDate localDate = LocalDate.now().plusDays(1);

        //when
        ArgumentException exception = assertThrows(ArgumentException.class, () -> diaryService.createDiary(localDate, text));

        assertThat(exception)
                .extracting("errorCode", "errorMessage")
                .contains(FUTURE_DATE_NOT_ALLOWED, FUTURE_DATE_NOT_ALLOWED.getDescription());
    }

    @Test
    @DisplayName("해당 날짜의 일기를 List 형태로 반환해야 한다")
    public void readDiaries() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        LocalDate anotherDate = LocalDate.of(2022, 05, 24);

        Diary diary1 = createDiaryBy(localDate, "맑음", "icon1", 222.1, "와1");
        Diary diary2 = createDiaryBy(localDate, "흐림", "icon2", 222.2, "와2");
        Diary diary3 = createDiaryBy(localDate, "좋음", "icon3", 222.3, "와3");
        Diary diary4 = createDiaryBy(anotherDate, "비", "icon4", 222.4, "와4");

        List<Diary> diaries = Arrays.asList(diary1, diary2, diary3, diary4);
        diaryRepository.saveAll(diaries);
        //when
        List<DiaryDto> findDiaries = diaryService.readDiaries(localDate);

        //then
        assertEquals(3, findDiaries.size());
        assertThat(findDiaries)
                .extracting("weather", "icon", "temperature", "text")
                .containsExactlyInAnyOrder(
                        tuple("맑음", "icon1", 222.1, "와1"),
                        tuple("흐림", "icon2", 222.2, "와2"),
                        tuple("좋음", "icon3", 222.3, "와3")
                );

    }

    @Test
    @DisplayName("미래의 일기를 조회할 때 예외가 발생한다")
    public void readDiaries_futureDateNotAllowed() throws Exception {
        //given
        LocalDate localDate = LocalDate.now().plusDays(1);

        //when
        ArgumentException exception = assertThrows(ArgumentException.class, () -> diaryService.readDiaries(localDate));

        assertThat(exception)
                .extracting("errorCode", "errorMessage")
                .contains(FUTURE_DATE_NOT_ALLOWED, FUTURE_DATE_NOT_ALLOWED.getDescription());

    }

    @Test
    @DisplayName("시작날짜와 종료날짜에 해당하는 일기를 List 형태로 반환해야 한다")
    public void readDiariesBetween() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2022, 05, 23);
        LocalDate localDate2 = LocalDate.of(2022, 05, 24);
        LocalDate endDate = LocalDate.of(2022, 05, 25);
        LocalDate anotherDate = LocalDate.of(2022, 05, 26);


        Diary diary1 = createDiaryBy(startDate, "맑음", "icon1", 222.1, "와1");
        Diary diary2 = createDiaryBy(localDate2, "흐림", "icon2", 222.2, "와2");
        Diary diary3 = createDiaryBy(endDate, "좋음", "icon3", 222.3, "와3");
        Diary diary4 = createDiaryBy(anotherDate, "비", "icon4", 222.4, "와4");

        List<Diary> diaries = Arrays.asList(diary1, diary2, diary3, diary4);
        diaryRepository.saveAll(diaries);
        //when
        List<DiaryDto> findDiaries = diaryService.readDiariesBetween(startDate, endDate);

        //then
        assertEquals(3, findDiaries.size());
        assertThat(findDiaries)
                .extracting("weather", "icon", "temperature", "text")
                .containsExactlyInAnyOrder(
                        tuple("맑음", "icon1", 222.1, "와1"),
                        tuple("흐림", "icon2", 222.2, "와2"),
                        tuple("좋음", "icon3", 222.3, "와3")
                );

    }

    @Test
    @DisplayName("일기 범위조회 시 시작값이 미래의 일기값이면 예외가 발생한다")
    public void readDiariesBetween_futureDateNotAllowed() throws Exception {
        //given
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);

        //when
        ArgumentException exception = assertThrows(ArgumentException.class, () -> diaryService.readDiariesBetween(startDate, endDate));

        assertThat(exception)
                .extracting("errorCode", "errorMessage")
                .contains(FUTURE_DATE_NOT_ALLOWED, FUTURE_DATE_NOT_ALLOWED.getDescription());

    }

    @Test
    @DisplayName("일기 범위조회 시 시작값이 종료값보다 미래면은 예외가 발생한다")
    public void readDiariesBetween_invalidDateRange() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2022, 05, 23);
        LocalDate endDate = startDate.minusDays(1);

        //when
        ArgumentException exception = assertThrows(ArgumentException.class, () -> diaryService.readDiariesBetween(startDate, endDate));

        assertThat(exception)
                .extracting("errorCode", "errorMessage")
                .contains(INVALID_DATE_RANGE, INVALID_DATE_RANGE.getDescription());

    }


    @Test
    @DisplayName("해당 날짜의 첫번째 일기 글을 새로 받아온 일기글로 수정해야 한다")
    public void updateDiary() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        String textForUpdate = "재밌다";

        Diary diary1 = createDiaryBy(localDate, "맑음", "icon1", 222.1, "와1");
        Diary diary2 = createDiaryBy(localDate, "흐림", "icon2", 222.2, "와2");
        Diary diary3 = createDiaryBy(localDate, "좋음", "icon3", 222.3, "와3");
        List<Diary> diaries = Arrays.asList(diary1, diary2, diary3);
        Diary savedDiary = diaryRepository.saveAll(diaries).get(0);

        //when
        DiaryDto updatedDiary = diaryService.updateOldestTextBy(localDate, textForUpdate);

        //then
        assertThat(updatedDiary)
                .extracting("id", "weather", "icon", "temperature", "text", "date")
                .contains(savedDiary.getId(), "맑음", "icon1", 222.1, textForUpdate, localDate);

    }

    @Test
    @DisplayName("일기 수정시 존재하지 않는 일기를 조회하면 예외가 발생한다")
    public void updateDiary_diaryDoesNotExist() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022, 05, 23);
        LocalDate targetDate = LocalDate.of(2022, 05, 24);

        Diary diary1 = createDiaryBy(localDate, "맑음", "icon1", 222.1, "와1");
        Diary diary2 = createDiaryBy(localDate, "흐림", "icon2", 222.2, "와2");
        List<Diary> diaries = Arrays.asList(diary1, diary2);
        diaryRepository.saveAll(diaries);

        //when
        ArgumentException exception = assertThrows(ArgumentException.class, () -> diaryService.updateOldestTextBy(targetDate, "수정"));

        assertThat(exception)
                .extracting("errorCode", "errorMessage")
                .contains(DIARY_DOES_NOT_EXIST, DIARY_DOES_NOT_EXIST.getDescription());

    }

    private static Diary createDiaryBy(LocalDate localDate, String weather, String icon, double temperature, String text) {
        return Diary.builder()
                .weather(weather)
                .icon(icon)
                .temperature(temperature)
                .text(text)
                .date(localDate)
                .build();
    }
}
