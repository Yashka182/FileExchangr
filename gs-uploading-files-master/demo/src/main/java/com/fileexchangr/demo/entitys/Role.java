package com.fileexchangr.demo.entitys;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    REGULAR_USER, PREMIUM_USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
