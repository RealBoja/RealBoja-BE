package com.realboja.backend.domain.room.dto;

import com.realboja.backend.domain.common.LastMet;
import com.realboja.backend.domain.common.Purpose;
import com.realboja.backend.domain.common.RoomType;
import com.realboja.backend.domain.common.Tone;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRoomRequest(
        @NotNull(message = "roomType은 필수입니다.")
        RoomType roomType,

        @NotNull(message = "lastMeeting은 필수입니다.")
        LastMet lastMeeting,

        @Min(value = 1, message = "roomSize는 1 이상이어야 합니다.")
        int roomSize,

        @NotNull(message = "purpose는 필수입니다.")
        Purpose purpose,

        @NotNull(message = "tone은 필수입니다.")
        Tone tone
) {}
