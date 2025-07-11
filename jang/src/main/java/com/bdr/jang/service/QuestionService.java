package com.bdr.jang.service;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.model.Question;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    Page<QuestionDTO> getAllQuestions(Pageable pageable);
    Page<QuestionDTO> getQuestionsByFilter(List<Integer> niveau, List<String> topics, Pageable pageable, boolean random);
    QuestionDTO getQuestionById(Integer id);     // pour vos API
    Question getQuestionEntityById(Integer id);
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    void deleteQuestion(Integer id);

    /**
     * Met à jour une question existante.
     * @param questionDto contient id, title, answer, level, topicName
     * @return le DTO mis à jour
     * @throws EntityNotFoundException si l’ID n’existe pas, ou si le topicName n’est pas trouvé
     */
    QuestionDTO updateQuestion(QuestionDTO questionDto);

    /**
     * Renvoie une page de questions adaptées à l’utilisateur.
     *
     * @param niveaux   filtres éventuels de niveaux
     * @param topics    filtres éventuels de topics
     * @param userId    identifiant de l’utilisateur (null si visiteur non connecté)
     * @param pageSize  nombre de questions à renvoyer (ex. 10)
     */
    Page<QuestionDTO> getAdaptiveQuestions(
            List<Integer> niveaux,
            List<String> topics,
            Long userId,
            int pageSize
    );
}
