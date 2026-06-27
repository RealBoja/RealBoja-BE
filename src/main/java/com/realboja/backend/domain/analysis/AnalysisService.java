package com.realboja.backend.domain.analysis;

import com.realboja.backend.domain.analysis.dto.AnalysisResponse;
import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.common.StatusType;
import com.realboja.backend.domain.reaction.Reaction;
import com.realboja.backend.domain.reaction.ReactionRepository;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnalysisService {

	private final RoomRepository roomRepository;
	private final ReactionRepository reactionRepository;

	@Transactional(readOnly = true)
	public AnalysisResponse getAnalysis(String roomCode) {
		Room room = roomRepository.findByRoomCode(roomCode)
			.orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다"));

		List<Reaction> reactions = reactionRepository.findAllByRoom(room);
		Map<ReactionType, Integer> reactionSummary = createEmptyReactionSummary();
		Map<ReactionType, List<String>> reactionParticipants = createEmptyReactionParticipants();

		for (Reaction reaction : reactions) {
			ReactionType reactionType = reaction.getReactionType();
			reactionSummary.merge(reactionType, 1, Integer::sum);
			reactionParticipants.get(reactionType).add(reaction.getNickname());
		}

		int participantCount = reactions.size();
		double participationRate = calculateParticipationRate(participantCount, room.getRoomSize());
		int temperature = calculateTemperature(reactions, room.getRoomSize());
		StatusType statusType = decideStatusType(temperature);

		return new AnalysisResponse(
			temperature,
			statusType,
			statusType.getLabel(),
			participantCount,
			room.getRoomSize(),
			participationRate,
			reactionSummary,
			reactionParticipants,
			createSummary(statusType),
			createNextAction(statusType)
		);
	}

	private Map<ReactionType, Integer> createEmptyReactionSummary() {
		Map<ReactionType, Integer> reactionSummary = new EnumMap<>(ReactionType.class);
		for (ReactionType reactionType : ReactionType.values()) {
			reactionSummary.put(reactionType, 0);
		}
		return reactionSummary;
	}

	private Map<ReactionType, List<String>> createEmptyReactionParticipants() {
		Map<ReactionType, List<String>> reactionParticipants = new EnumMap<>(ReactionType.class);
		for (ReactionType reactionType : ReactionType.values()) {
			reactionParticipants.put(reactionType, new ArrayList<>());
		}
		return reactionParticipants;
	}

	private double calculateParticipationRate(int participantCount, int roomSize) {
		if (roomSize <= 0) {
			return 0.0;
		}
		return Math.round(((double)participantCount / roomSize) * 100.0) / 100.0;
	}

	private int calculateTemperature(List<Reaction> reactions, int roomSize) {
		if (roomSize <= 0) {
			return 0;
		}

		double weightedScore = reactions.stream()
			.mapToDouble(reaction -> getReactionWeight(reaction.getReactionType()))
			.sum();

		return Math.min(100, (int)Math.round((weightedScore / roomSize) * 100));
	}

	private double getReactionWeight(ReactionType reactionType) {
		return switch (reactionType) {
			case REALLY_MEET -> 1.0;
			case PURPOSE_OK -> 0.8;
			case IF_SOMEONE_LEADS -> 0.6;
			case JUST_ALIVE -> 0.3;
		};
	}

	private StatusType decideStatusType(int temperature) {
		if (temperature <= 30) {
			return StatusType.COLD_ROOM;
		}
		if (temperature <= 50) {
			return StatusType.WOKE_UP;
		}
		if (temperature <= 70) {
			return StatusType.PURPOSE_MATCH;
		}
		return StatusType.NO_LEADER;
	}

	private String createSummary(StatusType statusType) {
		return switch (statusType) {
			case COLD_ROOM -> "아직 반응이 부족해요.";
			case WOKE_UP -> "조용하던 방에 첫 반응이 생겼어요.";
			case PURPOSE_MATCH -> "만날 마음이 조금씩 모이고 있어요.";
			case NO_LEADER -> "이 방은 만날 마음은 있지만, 아직 누가 먼저 잡을지 정해지지 않은 상태예요.";
			case MEET_SOON -> "이 정도면 실제 약속 조율로 넘어가도 좋아요.";
		};
	}

	private String createNextAction(StatusType statusType) {
		return switch (statusType) {
			case COLD_ROOM, WOKE_UP -> "먼저 생존신고를 받아보세요.";
			case PURPOSE_MATCH, NO_LEADER -> "밥 vs 카페처럼 쉬운 선택지부터 정하기";
			case MEET_SOON -> "날짜 후보를 던져보세요.";
		};
	}
}
