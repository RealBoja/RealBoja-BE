package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.reaction.Participant;
import com.realboja.backend.domain.room.RoomStep;

public record ParticipantResponse(
	Long participantId,
	String roomCode,
	String nickname,
	RoomStep currentStep
) {

	public static ParticipantResponse from(Participant participant) {
		return new ParticipantResponse(
			participant.getParticipantId(),
			participant.getRoom().getRoomCode(),
			participant.getNickname(),
			participant.getRoom().getCurrentStep()
		);
	}
}
