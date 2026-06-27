package com.realboja.backend.domain.schedule.dto;

import com.realboja.backend.domain.schedule.TimeSlot;

import java.util.List;

public record ScheduleResultResponse(
        int participantCount,
        TimeSlotResult topTimeSlot,
        List<TimeSlotResult> results,
        PlaceRecommendationGuide placeRecommendationGuide,
        List<PlaceRecommendation> placeRecommendations
) {
    public record TimeSlotResult(
            TimeSlot timeSlot,
            String label,
            int count,
            List<String> nicknames
    ) {}

    public record PlaceRecommendation(
            String area,
            String reason,
            List<String> hashtags
    ) {}

    public record PlaceRecommendationGuide(
            String status,
            String title,
            String description
    ) {}
}
