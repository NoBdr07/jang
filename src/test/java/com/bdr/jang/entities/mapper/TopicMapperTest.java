package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.dto.TopicDTO;
import com.bdr.jang.entities.model.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TopicMapperTest {

    @InjectMocks
    private TopicMapper topicMapper;

    Topic topic;
    TopicDTO topicDTO;

    @BeforeEach
    void setUp() {

        topic = Topic.builder()
                .id(1L)
                .name("topic")
                .build();

        topicDTO = TopicDTO.builder()
                .id(1L)
                .name("topic")
                .build();

    }

    @Test
    void mapToDto() {
        // WHEN
        TopicDTO result = topicMapper.mapToDto(topic);

        // THEN
        assertEquals(topicDTO, result);
    }

    @Test
    void mapToEntity() {
        // WHEN
        Topic result = topicMapper.mapToEntity(topicDTO);

        // THEN
        assertEquals(topic, result);
    }
}