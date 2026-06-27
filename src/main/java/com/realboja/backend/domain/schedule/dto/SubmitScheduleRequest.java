package com.realboja.backend.domain.schedule.dto;

import com.realboja.backend.domain.schedule.TimeSlot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitScheduleRequest(
        @NotBlank(message = "닉네임을 입력해 주세요.")
        @Size(max = 10, message = "닉네임은 10자 이내로 입력해 주세요.")
        String nickname,

        @NotEmpty(message = "시간대를 하나 이상 선택해 주세요.")
        List<TimeSlot> timeSlots
) {}
