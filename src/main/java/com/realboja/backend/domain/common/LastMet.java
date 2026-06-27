package com.realboja.backend.domain.common;

import lombok.Getter;

@Getter
public enum LastMet {

	ONE_MONTH("1달전"),
	HALF_YEAR("반년전"),
	OVER_ONE_YEAR("1년 넘음"),
	UNKNOWN("기억 안남");

	private final String label;

	LastMet(String label) {
		this.label = label;
	}
}
