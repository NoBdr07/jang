package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.service.TopicService;
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
