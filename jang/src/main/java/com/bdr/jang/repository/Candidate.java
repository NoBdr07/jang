package com.bdr.jang.repository;

import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.UserQuestionStat;

/**
 * Porte-objet interne pour l'algorithme adaptatif :
 * une Question + la stat utilisateur associée (peut être null).
 */
public record Candidate(
        Question question, UserQuestionStat stat
) {}
