package com.bdr.jang.entities.payload;

public record AuthResponse(String jwt, long expiresIn) {
}
