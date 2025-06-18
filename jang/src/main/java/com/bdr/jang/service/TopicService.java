package com.bdr.jang.service;

import com.bdr.jang.entities.dto.TopicDTO;
import com.bdr.jang.entities.model.Topic;

import java.util.List;

public interface TopicService {

    List<TopicDTO> getAllTopics();
    TopicDTO getTopicById(Integer id);
    TopicDTO getTopicByName(String name);
    Topic findTopicEntityByName(String name);
    TopicDTO createTopic(TopicDTO topicDTO);
    void deleteTopic(Integer id);
}
