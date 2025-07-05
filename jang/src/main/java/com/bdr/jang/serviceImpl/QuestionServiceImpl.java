package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.entities.mapper.QuestionMapper;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.entities.specification.QuestionSpecs;
import com.bdr.jang.repository.Candidate;
import com.bdr.jang.repository.QuestionRepository;
import com.bdr.jang.repository.TopicRepository;
import com.bdr.jang.service.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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
    public QuestionDTO getQuestionById(Integer id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found with id : " + id));
        return questionMapper.mapToDTO(question);
    }

    @Override
    public Question getQuestionEntityById(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question not found id=" + id));
    }

    @Override
    public QuestionDTO createQuestion(QuestionDTO questionDto) {
        Question question = questionMapper.mapToEntity(questionDto);
        Question saved = questionRepository.save(question);
        return questionMapper.mapToDTO(saved);
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Page<QuestionDTO> getQuestionsByFilter
            (List<Integer> niveaux, List<String> topics, Pageable pageable, boolean random) {

        List<String> trimmedTopics = null;
        if(topics != null) {
            trimmedTopics = topics.stream()
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        Specification<Question> spec = Specification
                .where(QuestionSpecs.hasLevels(niveaux))
                .and(QuestionSpecs.hasTopics(trimmedTopics));

        // aléa optionnel
        if (random) spec = spec.and(QuestionSpecs.randomOrder());

        Pageable pageRequest = random
                ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted())
                : pageable;

        return questionRepository.findAll(spec, pageRequest)
                .map(questionMapper::mapToDTO);
    }

    @Override
    public QuestionDTO updateQuestion(QuestionDTO dto) {
        Integer id = dto.getId();
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

    @Override
    public Page<QuestionDTO> getAdaptiveQuestions(
            List<Integer> niveaux,
            List<String> topics,
            Long userId,
            int pageSize) {

        // On récupère un pool élargi pour ensuite le trier
        int poolSize = pageSize * 8;
        Pageable poolPage = PageRequest.of(0, poolSize, Sort.unsorted());

        List<Candidate> pool = questionRepository
                .findCandidatesWithStats(niveaux, topics, userId, poolPage);

        // Pondération + tirage aléatoire
        LocalDate today = LocalDate.now();

        record Scored(Question question, double priority) {}

        List<Scored> scored = pool.stream()
                .map(c -> {
                    int score = c.stat() == null ? -1 : c.stat().getLastScore();
                    long days = c.stat() == null
                            ? Long.MAX_VALUE
                            : ChronoUnit.DAYS.between(
                                    c.stat().getLastTimeSeen(), today);

                    double base = switch (score) {
                        case -1 -> 1.0;   // jamais vue
                        case  0 -> 0.8;   // KO
                        case  1 -> 0.5;   // BOF
                        default -> 0.2;   // OK
                    };
                    double ageFactor = 1.0 + Math.min(days / 7.0, 3.0);
                    double priority = Math.random() / (base * ageFactor);

                    return new Scored(c.question(), priority);
                })
                .sorted(Comparator.comparingDouble(Scored::priority))
                .limit(pageSize)
                .toList();

        // Conversion en Page<QuestionDTO>
        List<QuestionDTO> dtos = scored.stream()
                .map(s -> questionMapper.mapToDTO(s.question()))
                .toList();

        return new PageImpl<>(dtos, PageRequest.of(0, pageSize), dtos.size());

    }

}











