package com.bdr.jang.entities.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(min=3, max=20) String username,
        @NotBlank @Size(min=6, max=50) String password
) {}

