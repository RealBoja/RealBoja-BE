package com.realboja.backend.domain.schedule.dto;

import com.realboja.backend.domain.schedule.TimeSlot;

import java.util.List;

public record SubmitScheduleResponse(
        String roomCode,
        String nickname,
        String location,
        List<TimeSlot> timeSlots
) {
    public static SubmitScheduleResponse of(String roomCode, String nickname, String location, List<TimeSlot> timeSlots) {
        return new SubmitScheduleResponse(roomCode, nickname, location, timeSlots);
    }
}
