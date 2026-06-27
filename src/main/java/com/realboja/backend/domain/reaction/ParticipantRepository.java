package com.realboja.backend.domain.reaction;

import com.realboja.backend.domain.room.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

	Optional<Participant> findByRoomAndParticipantId(Room room, Long participantId);

	boolean existsByRoomAndNickname(Room room, String nickname);
}
