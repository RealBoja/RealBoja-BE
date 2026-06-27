package com.realboja.backend.domain.reaction;

import com.realboja.backend.domain.room.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

	List<Reaction> findAllByRoom(Room room);

	Optional<Reaction> findByRoomAndNickname(Room room, String nickname);

	Optional<Reaction> findByRoomAndReactionId(Room room, Long reactionId);

	Optional<Reaction> findByRoomAndParticipant(Room room, Participant participant);
}
