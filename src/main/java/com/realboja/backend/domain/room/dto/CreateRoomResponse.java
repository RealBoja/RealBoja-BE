package com.realboja.backend.domain.room.dto;

import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomStep;

public record CreateRoomResponse(
        Long roomId,
        String roomCode,
        RoomStep currentStep
) {
    public static CreateRoomResponse from(Room room) {
        return new CreateRoomResponse(
                room.getRoomId(),
                room.getRoomCode(),
                room.getCurrentStep()
        );
    }
}
