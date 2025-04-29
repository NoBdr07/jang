package com.bdr.jang.entities.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {

    private Long id;

    @NotBlank(message = "Le titre ne doit pas être vide.")
    private String title;

    @NotBlank(message = "La réponse ne doit pas être vide.")
    private String answer;

    @Min(value = 1, message = "Le niveau minimum est 1.")
    @Max(value = 3, message = "Le niveau maximum est 3.")
    private int level;

    @NotBlank(message = "Le nom du topic ne doit pas être vide.")
    private String topicName;
}
