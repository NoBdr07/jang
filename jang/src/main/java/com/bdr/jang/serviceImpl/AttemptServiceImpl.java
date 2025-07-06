package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.model.Attempt;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.payload.AttemptLight;
import com.bdr.jang.repository.AttemptRepository;
import com.bdr.jang.service.AttemptService;
import com.bdr.jang.service.QuestionService;
import com.bdr.jang.service.UserQuestionStatService;
import com.bdr.jang.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepo;
    private final UserService userService;
    private final QuestionService questionService;
    private final UserQuestionStatService statService;

    public AttemptServiceImpl(
            AttemptRepository attemptRepo,
            UserService userService,
            QuestionService questionService,
            UserQuestionStatService statService) {

        this.attemptRepo      = attemptRepo;
        this.userService      = userService;
        this.questionService  = questionService;
        this.statService      = statService;
    }

    @Override
    public void recordSeries(Long userId, List<AttemptLight> attemptLights) {
        User user = userService.findUserEntityById(userId);
        LocalDateTime now = LocalDateTime.now();

        List<Attempt> attempts = attemptLights.stream()
                .map(d -> Attempt.builder()
                            .userId(userId)
                            .questionId(d.questionId().longValue())
                            .score(d.score())
                            .answeredAt(now)
                            .build())
                    .toList();
        attemptRepo.saveAll(attempts);

        attemptLights.forEach(d -> statService.recordAttempt(
                userId,
                d.questionId(),
                d.score(),
                now));

    }

}

















