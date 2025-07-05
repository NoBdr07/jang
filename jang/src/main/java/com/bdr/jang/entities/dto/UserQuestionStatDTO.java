package com.bdr.jang.entities.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestionStatDTO {

    private Integer id;

    @NotNull(message = "La date du dernier passage est requise")
    private LocalDateTime lastTimeSeen;

    @NotNull(message = "Le dernier score est requis")
    private Integer lastScore;

    @NotNull(message = "Le nombre de passages est requis")
    private Integer timeSeen;
}
