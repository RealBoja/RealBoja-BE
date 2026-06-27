package com.realboja.backend.domain.schedule;

import lombok.Getter;

@Getter
public enum TimeSlot {

    WEEKDAY_LUNCH("평일 점심"),
    WEEKDAY_DINNER("평일 저녁"),
    SATURDAY_LUNCH("토요일 점심"),
    SATURDAY_DINNER("토요일 저녁"),
    SUNDAY_LUNCH("일요일 점심"),
    SUNDAY_DINNER("일요일 저녁");

    private final String label;

    TimeSlot(String label) {
        this.label = label;
    }
}
