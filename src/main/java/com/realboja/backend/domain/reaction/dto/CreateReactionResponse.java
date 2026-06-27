package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.common.ReactionType;

public record CreateReactionResponse(
	Long reactionId,
	String nickname,
	ReactionType reactionType
) {
}
