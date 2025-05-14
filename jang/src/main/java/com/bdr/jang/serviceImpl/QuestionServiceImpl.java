package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.mapper.QuestionMapper;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.specification.QuestionSpecs;
import com.bdr.jang.repository.QuestionRepository;
import com.bdr.jang.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<QuestionDTO> getAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable)
                .map(questionMapper::mapToDTO);
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

    @Override
    public Page<QuestionDTO> getQuestionsByFilter
            (List<Integer> niveaux, List<String> topics, Pageable pageable) {
        Specification<Question> spec = Specification.where(QuestionSpecs.hasLevels(niveaux))
                .and(QuestionSpecs.hasTopics(topics));
        return questionRepository.findAll(spec, pageable)
                .map(questionMapper::mapToDTO);
    }


}
