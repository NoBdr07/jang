package com.bdr.jang.entities.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptDTO {

    private BigInteger id;

    @NotNull(message = "L’ID de l’utilisateur est requis")
    private Long userId;

    @NotNull(message = "L’ID de la question est requis")
    private Long questionId;

    @NotNull(message = "Le score est requis")
    private Integer score;

    @NotNull(message = "La date de réponse est requise")
    private LocalDateTime answeredAt;
}
