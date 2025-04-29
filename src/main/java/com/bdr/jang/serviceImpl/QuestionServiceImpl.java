package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.mapper.QuestionMapper;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.repository.QuestionRepository;
import com.bdr.jang.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(questionMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id : " + id));
        return questionMapper.mapToDTO(question);
    }

    @Override
    public QuestionDTO createQuestion(QuestionDTO questionDto) {
        Question question = questionMapper.mapToEntity(questionDto);
        Question saved = questionRepository.save(question);
        return questionMapper.mapToDTO(saved);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }


}
