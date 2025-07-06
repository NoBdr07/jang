package com.bdr.jang.service;

import java.time.LocalDateTime;

public interface UserQuestionStatService {

    /**
     * Met à jour ou crée la statistique pour (user, question).
     *
     * @param userId      identifiant utilisateur
     * @param questionId  identifiant question
     * @param score       0 = KO, 1 = BOF, 2 = OK
     * @param when        date/heure de la réponse
     */
    void recordAttempt(Long userId, Integer questionId, int score, LocalDateTime when);
}
