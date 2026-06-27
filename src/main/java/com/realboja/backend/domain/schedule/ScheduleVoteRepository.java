package com.realboja.backend.domain.schedule;

import com.realboja.backend.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleVoteRepository extends JpaRepository<ScheduleVote, Long> {

    List<ScheduleVote> findAllByRoom(Room room);

    @Modifying(clearAutomatically = true)
    @Query("delete from ScheduleVote sv where sv.room = :room and sv.nickname = :nickname")
    void deleteByRoomAndNickname(@Param("room") Room room, @Param("nickname") String nickname);
}
