package com.realboja.backend.domain.schedule.dto;

import com.realboja.backend.domain.schedule.TimeSlot;

import java.util.List;

public record ScheduleResultResponse(
        int participantCount,
        TimeSlotResult topTimeSlot,
        List<TimeSlotResult> results
) {
    public record TimeSlotResult(
            TimeSlot timeSlot,
            String label,
            int count,
            List<String> nicknames
    ) {}
}
