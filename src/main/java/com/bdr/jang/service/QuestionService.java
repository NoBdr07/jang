package com.bdr.jang.service;

import com.bdr.jang.entities.dto.QuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    Page<QuestionDTO> getAllQuestions(Pageable pageable);
    QuestionDTO getQuestionById(Long id);
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    void deleteQuestion(Long id);
}
