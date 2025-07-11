package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.mapper.QuestionMapper;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionMapper questionMapper;

    @InjectMocks
    private QuestionServiceImpl questionService;

    // Attributs de test partagés
    private Question q1, q2;
    private Topic t1, t2;
    private QuestionDTO q1DTO, q2DTO;
    private List<Question> questions;
    private List<QuestionDTO> questionsDTO;

    @BeforeEach
    void setUp() {
        // Création des entités
        t1 = Topic.builder()
                .id(1)
                .name("Topic1")
                .build();

        t2 = Topic.builder()
                .id(2)
                .name("Topic2")
                .build();

        q1 = Question.builder()
                .id(1)
                .title("Titre de la question 1 ?")
                .answer("Réponse de la quesion 1.")
                .level(1)
                .topic(t1)
                .build();

        q2 = Question.builder()
                .id(2)
                .title("Titre de la question 2 ?")
                .answer("Réponse de la quesion 2.")
                .level(1)
                .topic(t2)
                .build();


        questions = List.of(q1, q2);

        // Création des DTO correspondants
        q1DTO = QuestionDTO.builder()
                .id(1)
                .title("Titre de la question 1 ?")
                .answer("Réponse de la quesion 1.")
                .level(1)
                .topicName("Topic1")
                .build();

        q2DTO = QuestionDTO.builder()
                .id(2)
                .title("Titre de la question 2 ?")
                .answer("Réponse de la quesion 2.")
                .level(1)
                .topicName("Topic2")
                .build();

        questionsDTO = List.of(q1DTO, q2DTO);
    }

    @Test
    void getAllQuestions_shouldReturnPagedDTOs() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 2);
        Page<Question> questionPage = new PageImpl<>(questions, pageable, questions.size());

        when(questionRepository.findAll(pageable)).thenReturn(questionPage);
        when(questionMapper.mapToDTO(q1)).thenReturn(q1DTO);
        when(questionMapper.mapToDTO(q2)).thenReturn(q2DTO);

        // WHEN
        Page<QuestionDTO> result = questionService.getAllQuestions(pageable);

        // THEN
        assertThat(result.getContent())
                .hasSize(2)
                .containsExactly(q1DTO, q2DTO);
        // Les méta-données de pagination
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(2);

        verify(questionRepository, times(1)).findAll(pageable);
        verify(questionMapper, times(1)).mapToDTO(q1);
        verify(questionMapper, times(1)).mapToDTO(q2);
        verifyNoMoreInteractions(questionRepository, questionMapper);
    }

    @Test
    void getQuestionById_shouldReturnQuestionDTO() {
        // GIVEN
        when(questionRepository.findById(1)).thenReturn(Optional.of(q1));
        when(questionMapper.mapToDTO(q1)).thenReturn(q1DTO);

        // WHEN
        QuestionDTO result = questionService.getQuestionById(1);

        // THEN
        assertEquals(result, q1DTO);
        verify(questionRepository, times(1)).findById(1);
        verify(questionMapper, times(1)).mapToDTO(q1);
    }

    @Test
    void getQuestionById_shouldThrowWhenNotFound() {
        // GIVEN
        when(questionRepository.findById(1)).thenReturn(Optional.empty());

        // WHEN & THEN
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class, () -> questionService.getQuestionById(1)
        );

        assertTrue(ex.getMessage().contains("Question not found with id : 1"));
        verify(questionRepository).findById(1);

    }

    @Test
    void createQuestion_shouldReturnQuestionDTO() {
        // GIVEN
        when(questionMapper.mapToEntity(q1DTO)).thenReturn(q1);
        when(questionRepository.save(q1)).thenReturn(q1);
        when(questionMapper.mapToDTO(q1)).thenReturn(q1DTO);

        // WHEN
        QuestionDTO result = questionService.createQuestion(q1DTO);

        // THEN
        assertEquals(result, q1DTO);
        verify(questionMapper, times(1)).mapToEntity(q1DTO);
        verify(questionMapper, times(1)).mapToDTO(q1);
        verify(questionRepository, times(1)).save(q1);
    }

    @Test
    void deleteQuestion_shouldInvokeRepositoryDeleteById() {
        // GIVEN
        Integer idToDelete = 42;
        doNothing().when(questionRepository).deleteById(idToDelete);

        // WHEN
        questionService.deleteQuestion(idToDelete);

        // THEN
        verify(questionRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void getQuestionsByFilter_shouldReturnFilteredQuestions() {
        // GIVEN
        List<Integer> niveaux = List.of(1);
        List<String> topics = List.of("Topic1", "Topic2");

        Pageable pageable = PageRequest.of(0, 2);
        Page<Question> questionPage = new PageImpl<>(questions, pageable, questions.size());

        Page<QuestionDTO> questionDTOPage = new PageImpl<>(List.of(q1DTO, q2DTO), pageable, questions.size());

        when(questionRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(questionPage);
        when(questionMapper.mapToDTO(q1)).thenReturn(q1DTO);
        when(questionMapper.mapToDTO(q2)).thenReturn(q2DTO);

        // WHEN
        Page<QuestionDTO> result = questionService.getQuestionsByFilter(niveaux, topics, pageable, true);

        // THEN
        assertEquals(questionDTOPage, result);
    }
}