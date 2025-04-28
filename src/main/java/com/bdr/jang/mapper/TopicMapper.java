package com.bdr.jang.mapper;

import com.bdr.jang.dto.TopicDTO;
import com.bdr.jang.model.Topic;
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
