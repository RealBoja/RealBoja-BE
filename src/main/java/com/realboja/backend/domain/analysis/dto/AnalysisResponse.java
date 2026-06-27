package com.realboja.backend.domain.analysis.dto;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.common.StatusType;
import java.util.Map;

public record AnalysisResponse(
	Integer temperature,
	StatusType statusType,
	String statusLabel,
	Integer participantCount,
	Integer roomSize,
	Double participationRate,
	Map<ReactionType, Integer> reactionSummary,
	String summary,
	String nextAction
) {
}
