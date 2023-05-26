package com.zerobase.weather.repository.diary;

import com.zerobase.weather.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByDate(LocalDate date);

    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Diary> getFirstByDate(LocalDate date);
}
