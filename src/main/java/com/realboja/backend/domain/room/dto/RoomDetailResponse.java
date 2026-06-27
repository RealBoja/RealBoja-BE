package com.realboja.backend.domain.room.dto;

import com.realboja.backend.domain.common.Purpose;
import com.realboja.backend.domain.common.RoomType;
import com.realboja.backend.domain.common.Tone;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomStep;

import java.time.LocalDateTime;

public record RoomDetailResponse(
        Long roomId,
        String roomCode,
        RoomType roomType,
        int roomSize,
        Purpose purpose,
        Tone tone,
        RoomStep currentStep,
        LocalDateTime createdAt
) {
    public static RoomDetailResponse from(Room room) {
        return new RoomDetailResponse(
                room.getRoomId(),
                room.getRoomCode(),
                room.getRoomType(),
                room.getRoomSize(),
                room.getPurpose(),
                room.getTone(),
                room.getCurrentStep(),
                room.getCreatedAt()
        );
    }
}
