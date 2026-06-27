package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum CardType {

	WAKE("깨우기 카드");

	private final String label;

	CardType(String label) {
		this.label = label;
	}
}
