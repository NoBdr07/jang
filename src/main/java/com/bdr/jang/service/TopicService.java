package com.bdr.jang.service;

import com.bdr.jang.dto.TopicDTO;
import com.bdr.jang.model.Topic;

import java.util.List;

public interface TopicService {

    List<TopicDTO> getAllTopics();
    TopicDTO getTopicById(Long id);
    TopicDTO getTopicByName(String name);
    Topic findTopicEntityByName(String name);
    TopicDTO createTopic(TopicDTO topicDTO);
    void deleteTopic(Long id);
}
