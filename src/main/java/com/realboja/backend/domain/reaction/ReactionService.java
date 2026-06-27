package com.realboja.backend.domain.reaction;

import com.realboja.backend.domain.common.ReactionType;
import com.realboja.backend.domain.reaction.dto.CreateParticipantRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionResponse;
import com.realboja.backend.domain.reaction.dto.ParticipantResponse;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
import com.realboja.backend.domain.room.RoomStep;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReactionService {

	private final RoomRepository roomRepository;
	private final ReactionRepository reactionRepository;
	private final ParticipantRepository participantRepository;

	@Transactional
	public ParticipantResponse createParticipant(String roomCode, CreateParticipantRequest request) {
		Room room = roomRepository.findByRoomCode(roomCode)
			.orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다"));

		Participant participant = Participant.builder()
			.room(room)
			.nickname(request.nickname())
			.build();

		return ParticipantResponse.from(participantRepository.save(participant));
	}

	@Transactional
	public CreateReactionResponse createReaction(String roomCode, CreateReactionRequest request) {
		Room room = roomRepository.findByRoomCode(roomCode)
			.orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다"));

		if (room.getCurrentStep() != RoomStep.WARMING) {
			throw new IllegalStateException("이미 일정 조율 단계로 넘어가 반응을 받을 수 없습니다.");
		}

		Participant participant = participantRepository.findByRoomAndParticipantId(room, request.participantId())
			.orElseThrow(() -> new IllegalArgumentException("참가자를 찾을 수 없습니다"));

		Reaction reaction = reactionRepository.findByRoomAndParticipant(room, participant)
			.map(existingReaction -> {
				existingReaction.updateReactionType(request.reactionType());
				return existingReaction;
			})
			.orElseGet(() -> Reaction.builder()
				.room(room)
				.participant(participant)
				.nickname(participant.getNickname())
				.reactionType(request.reactionType())
				.build());

		reactionRepository.save(reaction);
		List<Reaction> allReactions = reactionRepository.findAllByRoom(room);
		int temperature = calculateTemperature(allReactions, room.getRoomSize());

		return CreateReactionResponse.of(reaction, temperature);
	}

	@Transactional(readOnly = true)
	public ParticipantResponse getParticipant(String roomCode, Long participantId) {
		Room room = roomRepository.findByRoomCode(roomCode)
			.orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다"));

		Participant participant = participantRepository.findByRoomAndParticipantId(room, participantId)
			.orElseThrow(() -> new IllegalArgumentException("참가자를 찾을 수 없습니다"));

		return ParticipantResponse.from(participant);
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
