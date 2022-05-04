package com.daangndaangn.common.api.entity.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role {

    USER("ROLE_USER");

    private final String value;

    public String value() {
        return value;
    }

    public static Role from(String name) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }

}
