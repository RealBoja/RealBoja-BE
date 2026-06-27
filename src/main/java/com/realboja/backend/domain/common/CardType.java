package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum CardType {

	WAKE("깨우기 카드", 1),
	SCHEDULE("일정 조율 카드", 2);

	private final String label;
	private final int stepOrder;

	CardType(String label, int stepOrder) {
		this.label = label;
		this.stepOrder = stepOrder;
	}
}
