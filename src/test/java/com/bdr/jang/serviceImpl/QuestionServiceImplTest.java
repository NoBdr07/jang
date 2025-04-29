package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.mapper.QuestionMapper;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.repository.QuestionRepository;
import org.hibernate.validator.constraints.Mod10Check;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private Topic t1;
    private QuestionDTO q1DTO, q2DTO;
    private List<Question> questions;
    private List<QuestionDTO> questionsDTO;

    @BeforeEach
    void setUp() {
        // Création des entités
        t1 = Topic.builder()
                .id(1L)
                .name("Topic1")
                .build();

        q1 = Question.builder()
                .id(1L)
                .title("Titre de la question 1 ?")
                .answer("Réponse de la quesion 1.")
                .level(1)
                .topic(t1)
                .build();

        q2 = Question.builder()
                .id(2L)
                .title("Titre de la question 2 ?")
                .answer("Réponse de la quesion 2.")
                .level(1)
                .topic(t1)
                .build();

        questions = List.of(q1, q2);

        // Création des DTO correspondants
        q1DTO = QuestionDTO.builder()
                .id(1L)
                .title("Titre de la question 1 ?")
                .answer("Réponse de la quesion 1.")
                .level(1)
                .topicName("Topic1")
                .build();

        q2DTO = QuestionDTO.builder()
                .id(2L)
                .title("Titre de la question 2 ?")
                .answer("Réponse de la quesion 2.")
                .level(1)
                .topicName("Topic1")
                .build();

        questionsDTO = List.of(q1DTO, q2DTO);
    }

    @Test
    void getAllQuestions_shouldReturnListOfDTOs() {
        // GIVEN
        when(questionRepository.findAll()).thenReturn(questions);
        when(questionMapper.mapToDTO(q1)).thenReturn(q1DTO);
        when(questionMapper.mapToDTO(q2)).thenReturn(q2DTO);

        // WHEN
        List<QuestionDTO> result = questionService.getAllQuestions();

        // THEN
        assertThat(result).hasSize(2).containsExactly(q1DTO, q2DTO);
        verify(questionRepository, times(1)).findAll();
        verify(questionMapper, times(1)).mapToDTO(q1);
        verify(questionMapper, times(1)).mapToDTO(q2);
    }

    @Test
    void getQuestionById() {
        // GIVEN
        when(questionRepository.findById(1L)).thenReturn(Optional.of(q1));
        when(questionMapper.mapToDTO(q1)).thenReturn(q1DTO);

        // WHEN
        QuestionDTO result = questionService.getQuestionById(1L);

        // THEN
        assertEquals(result, q1DTO);
        verify(questionRepository, times(1)).findById(1L);
        verify(questionMapper, times(1)).mapToDTO(q1);
    }

    @Test
    void createQuestion() {
        // GIVEN

        // WHEN

        // THEN

    }

    @Test
    void deleteQuestion() {
    }
}