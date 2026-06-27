package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.reaction.Reaction;

public record CreateReactionResponse(
	Long participantId,
	String roomCode,
	ReactionType reactionType,
	Integer temperature
) {

	public static CreateReactionResponse of(Reaction reaction, Integer temperature) {
		return new CreateReactionResponse(
			reaction.getReactionId(),
			reaction.getRoom().getRoomCode(),
			reaction.getReactionType(),
			temperature
		);
	}
}
