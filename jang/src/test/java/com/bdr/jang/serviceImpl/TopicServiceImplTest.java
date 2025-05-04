package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.TopicDTO;
import com.bdr.jang.entities.mapper.TopicMapper;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplUnitTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private TopicServiceImpl topicService;

    private Topic topic1;
    private Topic topic2;
    private TopicDTO dto1;
    private TopicDTO dto2;

    @BeforeEach
    void setUp() {
        topic1 = Topic.builder()
                .id(1L)
                .name("Sujet A")
                .build();

        topic2 = Topic.builder()
                .id(2L)
                .name("Sujet B")
                .build();

        dto1 = new TopicDTO(1L, "Sujet A");
        dto2 = new TopicDTO(2L, "Sujet B");
    }

    @Test
    void findTopicEntityByName_found() {
        given(topicRepository.findByName("Sujet A")).willReturn(Optional.of(topic1));

        Topic result = topicService.findTopicEntityByName("Sujet A");

        assertNotNull(result);
        assertEquals(topic1, result);
    }

    @Test
    void findTopicEntityByName_notFound() {
        given(topicRepository.findByName("Inconnu")).willReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> topicService.findTopicEntityByName("Inconnu"));

        assertTrue(ex.getMessage().contains("Topic not found: Inconnu"));
    }

    @Test
    void getAllTopics_returnsDtoList() {
        given(topicRepository.findAll()).willReturn(List.of(topic1, topic2));
        given(topicMapper.mapToDto(topic1)).willReturn(dto1);
        given(topicMapper.mapToDto(topic2)).willReturn(dto2);

        List<TopicDTO> result = topicService.getAllTopics();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void getTopicById_found() {
        given(topicRepository.findById(1L)).willReturn(Optional.of(topic1));
        given(topicMapper.mapToDto(topic1)).willReturn(dto1);

        TopicDTO result = topicService.getTopicById(1L);

        assertNotNull(result);
        assertEquals(dto1, result);
    }

    @Test
    void getTopicById_notFound() {
        given(topicRepository.findById(99L)).willReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> topicService.getTopicById(99L));

        assertTrue(ex.getMessage().contains("No topic found with id : 99"));
    }

    @Test
    void getTopicByName_found() {
        given(topicRepository.findByName("Sujet B")).willReturn(Optional.of(topic2));
        given(topicMapper.mapToDto(topic2)).willReturn(dto2);

        TopicDTO result = topicService.getTopicByName("Sujet B");

        assertNotNull(result);
        assertEquals(dto2, result);
    }

    @Test
    void getTopicByName_notFound() {
        given(topicRepository.findByName("X")).willReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> topicService.getTopicByName("X"));

        assertTrue(ex.getMessage().contains("No topic found with name : X"));
    }

    @Test
    void createTopic_success() {
        TopicDTO newDto = new TopicDTO(null, "Nouveau");
        Topic newTopic = new Topic();
        newTopic.setName("Nouveau");
        Topic savedTopic = new Topic();
        savedTopic.setId(3L);
        savedTopic.setName("Nouveau");
        TopicDTO savedDto = new TopicDTO(3L, "Nouveau");

        given(topicMapper.mapToEntity(newDto)).willReturn(newTopic);
        given(topicRepository.save(newTopic)).willReturn(savedTopic);
        given(topicMapper.mapToDto(savedTopic)).willReturn(savedDto);

        TopicDTO result = topicService.createTopic(newDto);

        assertNotNull(result);
        assertEquals(savedDto, result);
    }

    @Test
    void deleteTopic_invokesRepository() {
        doNothing().when(topicRepository).deleteById(1L);

        topicService.deleteTopic(1L);

        verify(topicRepository, times(1)).deleteById(1L);
    }
}
