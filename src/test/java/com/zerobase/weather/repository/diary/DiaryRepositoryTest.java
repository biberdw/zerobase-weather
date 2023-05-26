package com.zerobase.weather.repository.diary;

import com.zerobase.weather.domain.diary.Diary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DiaryRepositoryTest {

    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    @DisplayName("날짜가 같은 일기 중 가장 오래된 일기가 조회돼야 한다")
    public void getFirstByDate() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022,05,23);

        Diary diary1 = createDiaryBy(localDate, "맑음", "icon1", 222.1, "와1");
        Diary diary2 = createDiaryBy(localDate, "흐림", "icon2", 222.2, "와2");
        Diary diary3 = createDiaryBy(localDate, "좋음", "icon3", 222.3, "와3");
        List<Diary> diaries = Arrays.asList(diary1, diary2, diary3);
        diaryRepository.saveAll(diaries);

        //when
        Diary findDiary = diaryRepository.getFirstByDate(localDate).orElseThrow(() ->
                new IllegalArgumentException("조회된 데이터 없음"));

        //then
        assertThat(findDiary)
                .extracting("id","weather","icon","temperature","text")
                .contains(findDiary.getId(),"맑음","icon1",222.1,"와1");

    }

    @Test
    @DisplayName("해당 날짜의 모든 일기를 지워야 한다")
    public void deleteDiariesByDate() throws Exception {
        //given
        LocalDate localDate = LocalDate.of(2022,05,23);
        LocalDate anotherDate = LocalDate.of(2023,05,23);

        Diary diary1 = createDiaryBy(localDate, "맑음", "icon1", 222.1, "와1");
        Diary diary2 = createDiaryBy(localDate, "흐림", "icon2", 222.2, "와2");
        Diary diary3 = createDiaryBy(anotherDate,"비", "icon3", 222.3, "와3");
        List<Diary> diaries = Arrays.asList(diary1, diary2, diary3);
        diaryRepository.saveAll(diaries);

        //when
        int deletedRows = diaryRepository.deleteDiariesByDate(localDate);

        List<Diary> findDiaries = diaryRepository.findAll();

        //then
        assertThat(findDiaries.size()).isEqualTo(1);
        assertThat(findDiaries.get(0).getWeather()).isEqualTo("비");
        assertThat(deletedRows).isEqualTo(2);
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