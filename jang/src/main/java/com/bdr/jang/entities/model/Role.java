package com.bdr.jang.entities.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Enumeration of application roles for user authorization.
 * Each role can be converted to a Spring Security authority string via the authority() method.
 */
public enum Role {
    USER,
    ADMIN;

    /**
     * Returns the Spring Security authority representation for this role,
     * prefixed with "ROLE_" as expected by default in SimpleGrantedAuthority.
     *
     * @return the authority string for this role
     */
    public String authority() {
        return "ROLE_" + name();
    }
}