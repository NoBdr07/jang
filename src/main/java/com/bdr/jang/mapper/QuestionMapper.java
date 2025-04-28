package com.bdr.jang.mapper;

import com.bdr.jang.dto.QuestionDTO;
import com.bdr.jang.model.Question;
import com.bdr.jang.service.TopicService;
import com.bdr.jang.serviceImpl.TopicServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    private final TopicService topicService;

    public QuestionMapper( TopicService topicService) {
        this.topicService = topicService;
    }

    public QuestionDTO mapToDTO(Question question) {
        return QuestionDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .answer(question.getAnswer())
                .level(question.getLevel())
                .topicName(question.getTopic().getName())
                .build();
    }

    public Question mapToEntity(QuestionDTO questionDTO) {
        return Question.builder()
                .id(questionDTO.getId())
                .title(questionDTO.getTitle())
                .answer(questionDTO.getAnswer())
                .level(questionDTO.getLevel())
                .topic(topicService.findTopicEntityByName(questionDTO.getTopicName()))
                .build();
    }
}
