package com.realboja.backend.domain.card;

import com.realboja.backend.domain.common.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByRoomIdAndCardType(Long roomId, CardType cardType);
}
