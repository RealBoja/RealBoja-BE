package com.realboja.backend.domain.reaction.dto;

import com.realboja.backend.domain.common.ReactionType;
import jakarta.validation.constraints.NotNull;

public record CreateReactionRequest(
	@NotNull(message = "participantId는 필수입니다.")
	Long participantId,

	@NotNull(message = "반응을 선택해 주세요.")
	ReactionType reactionType
) {
}
