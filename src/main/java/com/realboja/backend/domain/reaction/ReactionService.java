package com.realboja.backend.domain.reaction;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.reaction.dto.CreateReactionRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionResponse;
import com.realboja.backend.domain.reaction.dto.ParticipantResponse;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

	private final RoomRepository roomRepository;
	private final ReactionRepository reactionRepository;

	@Transactional
	public CreateReactionResponse createReaction(String roomCode, CreateReactionRequest request) {
		Room room = roomRepository.findByRoomCode(roomCode)
			.orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다"));

		Reaction reaction = reactionRepository.findByRoomAndNickname(room, request.nickname())
			.map(existingReaction -> {
				existingReaction.updateReactionType(request.reactionType());
				return existingReaction;
			})
			.orElseGet(() -> Reaction.builder()
				.room(room)
				.nickname(request.nickname())
				.reactionType(request.reactionType())
				.build());

		Reaction savedReaction = reactionRepository.save(reaction);
		int temperature = calculateTemperature(reactionRepository.findAllByRoom(room), room.getRoomSize());

		return CreateReactionResponse.of(savedReaction, temperature);
	}

	@Transactional(readOnly = true)
	public ParticipantResponse getParticipant(String roomCode, Long participantId) {
		Room room = roomRepository.findByRoomCode(roomCode)
			.orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다"));

		Reaction reaction = reactionRepository.findByRoomAndReactionId(room, participantId)
			.orElseThrow(() -> new IllegalArgumentException("참가자를 찾을 수 없습니다"));

		return ParticipantResponse.from(reaction);
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
}
