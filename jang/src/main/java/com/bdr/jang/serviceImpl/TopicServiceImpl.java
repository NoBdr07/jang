package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.TopicDTO;
import com.bdr.jang.entities.mapper.TopicMapper;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.repository.TopicRepository;
import com.bdr.jang.service.TopicService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public TopicServiceImpl (TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    /**
     * Méthode interne utilisée par les mappers ou la logique métier
     */
    @Transactional(readOnly = true)
    public Topic findTopicEntityByName(String name) {
        return topicRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found: " + name));
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAll()
                .stream()
                .map(topicMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TopicDTO getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No topic found with id : " + id));
        return topicMapper.mapToDto(topic);
    }

    @Override
    public TopicDTO getTopicByName(String name) {
        Topic topic = topicRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("No topic found with name : " + name));
        return topicMapper.mapToDto(topic);
    }

    @Override
    public TopicDTO createTopic(TopicDTO topicDTO) {
        Topic topic = topicMapper.mapToEntity(topicDTO);
        Topic saved = topicRepository.save(topic);
        return topicMapper.mapToDto(saved);
    }

    @Override
    public void deleteTopic(Integer id) {
        topicRepository.deleteById(id);
    }
}
