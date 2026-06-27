package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.reaction.Reaction;

public record CreateReactionResponse(
	Long reactionId,
	String nickname,
	ReactionType reactionType
) {

	public static CreateReactionResponse from(Reaction reaction) {
		return new CreateReactionResponse(
			reaction.getReactionId(),
			reaction.getNickname(),
			reaction.getReactionType()
		);
	}
}
