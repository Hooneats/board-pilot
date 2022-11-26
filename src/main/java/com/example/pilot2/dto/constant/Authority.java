package com.example.pilot2.dto.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authority {
    USER("ROLE_USER", "유저_계정_입니다."),
    ADMIN("ROLE_ADMIN", "관리자_계정_입니다.");

    private final String role;
    private final String description;
}
