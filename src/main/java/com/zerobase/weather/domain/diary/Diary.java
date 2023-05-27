package com.zerobase.weather.domain.diary;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Diary {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "diary_id")
    private Long id;
    private String weather;
    private String icon;
    private Double temperature;
    private String text;
    private LocalDate date;

    @Builder
    private Diary(String weather, String icon, Double temperature, String text, LocalDate date) {
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
        this.text = text;
        this.date = date;
    }

    public void updateText(String text) {
        this.text = text;
    }
}
