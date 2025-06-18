package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionMapperTest {

    Question question ;
    QuestionDTO questionDTO;
    Topic topic ;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private QuestionMapper questionMapper;

    @BeforeEach
    void setUp() {

        topic = Topic.builder()
                .id(1)
                .name("topic")
                .build();

        question = Question.builder()
                .id(1)
                .title("title")
                .answer("answer")
                .level(1)
                .topic(topic)
                .build();

        questionDTO = QuestionDTO.builder()
                .id(1)
                .title("title")
                .answer("answer")
                .level(1)
                .topicName("topic")
                .build();

    }

    @Test
    void mapToDTO_shouldReturnDTO() {
        // WHEN
        QuestionDTO result = questionMapper.mapToDTO(question);

        // THEN
        assertEquals(questionDTO, result);
    }

    @Test
    void mapToEntity_shouldReturnEntity() {
        // GIVEN
        when(topicService.findTopicEntityByName("topic")).thenReturn(topic);

        // WHEN
        Question result = questionMapper.mapToEntity(questionDTO);

        // THEN
        assertEquals(question, result);

    }
}