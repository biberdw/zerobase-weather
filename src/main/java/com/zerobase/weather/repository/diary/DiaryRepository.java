package com.zerobase.weather.repository.diary;

import com.zerobase.weather.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
