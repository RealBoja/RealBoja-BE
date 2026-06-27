package com.realboja.backend.domain.reaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateParticipantRequest(
	@NotBlank(message = "닉네임을 입력해 주세요.")
	@Size(max = 10, message = "닉네임은 10자 이내로 입력해 주세요.")
	String nickname
) {
}
