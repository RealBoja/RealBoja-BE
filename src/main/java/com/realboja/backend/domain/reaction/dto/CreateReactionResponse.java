package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.reaction.Reaction;
import com.realboja.backend.domain.room.RoomStep;

public record CreateReactionResponse(
	Long participantId,
	String roomCode,
	String nickname,
	ReactionType reactionType,
	Integer temperature,
	RoomStep currentStep
) {

	public static CreateReactionResponse of(Reaction reaction, Integer temperature) {
		return new CreateReactionResponse(
			reaction.getParticipant().getParticipantId(),
			reaction.getRoom().getRoomCode(),
			reaction.getNickname(),
			reaction.getReactionType(),
			temperature,
			reaction.getRoom().getCurrentStep()
		);
	}
}
