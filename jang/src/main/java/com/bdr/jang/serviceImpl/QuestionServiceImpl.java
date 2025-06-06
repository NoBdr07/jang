package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.mapper.QuestionMapper;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.entities.specification.QuestionSpecs;
import com.bdr.jang.repository.QuestionRepository;
import com.bdr.jang.repository.TopicRepository;
import com.bdr.jang.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final TopicRepository topicRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper, TopicRepository topicRepository) {
        this.questionMapper = questionMapper;
        this.questionRepository = questionRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public Page<QuestionDTO> getAllQuestions(Pageable pageable) {
        // on fixe toujours 20 questions par page et on neutralise tout tri
        PageRequest p20 = PageRequest.of(
                pageable.getPageNumber(),
                20,
                Sort.unsorted()
        );

        // on passe juste la spec randomOrder()
        return questionRepository
                .findAll(QuestionSpecs.randomOrder(), p20)
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

        List<String> trimmedTopics = null;
        if(topics != null) {
            trimmedTopics = topics.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        Specification<Question> spec = Specification
                .where(QuestionSpecs.hasLevels(niveaux))
                .and(QuestionSpecs.hasTopics(trimmedTopics))
                .and(QuestionSpecs.randomOrder());
        return questionRepository.findAll(spec, pageable)
                .map(questionMapper::mapToDTO);
    }

    @Override
    public QuestionDTO updateQuestion(QuestionDTO dto) {
        Long id = dto.getId();
        // 1. Vérifier que la question existe
        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id : " + id));

        // 2. Récupérer / valider le Topic par son nom
        Topic topic = topicRepository.findByName(dto.getTopicName())
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with name : " + dto.getTopicName()));

        // 3. Mettre à jour chaque champ nécessaire
        existing.setTitle(dto.getTitle());
        existing.setAnswer(dto.getAnswer());
        existing.setLevel(dto.getLevel());
        existing.setTopic(topic);

        // 4. Sauvegarder l’entité mise à jour
        Question saved = questionRepository.save(existing);

        // 5. Retourner le DTO
        return questionMapper.mapToDTO(saved);
    }


}
