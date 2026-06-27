package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.reaction.Reaction;

public record ParticipantResponse(
	Long participantId,
	String roomCode,
	String nickname
) {

	public static ParticipantResponse from(Reaction reaction) {
		return new ParticipantResponse(
			reaction.getReactionId(),
			reaction.getRoom().getRoomCode(),
			reaction.getNickname()
		);
	}
}
