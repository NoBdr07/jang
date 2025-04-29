package com.bdr.jang.service;

import com.bdr.jang.entities.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {

    List<QuestionDTO> getAllQuestions();
    QuestionDTO getQuestionById(Long id);
    QuestionDTO createQuestion(QuestionDTO questionDTO);
    void deleteQuestion(Long id);
}
