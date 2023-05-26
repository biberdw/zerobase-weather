package com.zerobase.weather.repository.diary;

import com.zerobase.weather.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findAllByDate(LocalDate date);

    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Diary> getFirstByDate(LocalDate date);

    @Modifying(clearAutomatically = true)
    @Query("delete from Diary d where d.date = :date")
    int deleteDiariesByDate(@Param("date") LocalDate date);
}
