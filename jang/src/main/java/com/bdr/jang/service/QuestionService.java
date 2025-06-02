package com.bdr.jang.service;

import com.bdr.jang.entities.dto.QuestionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    Page<QuestionDTO> getAllQuestions(Pageable pageable);
    Page<QuestionDTO> getQuestionsByFilter(List<Integer> niveau, List<String> topics, Pageable pageable);
    QuestionDTO getQuestionById(Long id);
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    void deleteQuestion(Long id);

    /**
     * Met à jour une question existante.
     * @param questionDto contient id, title, answer, level, topicName
     * @return le DTO mis à jour
     * @throws EntityNotFoundException si l’ID n’existe pas, ou si le topicName n’est pas trouvé
     */
    QuestionDTO updateQuestion(QuestionDTO questionDto);
}
