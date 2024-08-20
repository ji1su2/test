package com.web.demo.config;

import lombok.Getter;

//FIXME #REFACT: 사용자 권한
@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
