package com.realboja.backend.domain.reaction;

import com.realboja.backend.domain.reaction.dto.CreateReactionRequest;
import com.realboja.backend.domain.reaction.dto.CreateReactionResponse;
import com.realboja.backend.domain.room.Room;
import com.realboja.backend.domain.room.RoomRepository;
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
		return CreateReactionResponse.from(savedReaction);
	}
}
