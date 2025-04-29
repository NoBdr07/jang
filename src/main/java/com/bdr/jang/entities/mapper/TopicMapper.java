package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.TopicDTO;
import com.bdr.jang.entities.model.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {

    public TopicDTO mapToDto(Topic topic) {
        return TopicDTO.builder()
                .id(topic.getId())
                .name(topic.getName())
                .build();
    }

    public Topic mapToEntity(TopicDTO topicDTO) {
        return Topic.builder()
                .id(topicDTO.getId())
                .name(topicDTO.getName())
                .build();
    }
}
