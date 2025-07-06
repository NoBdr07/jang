package com.bdr.jang.serviceImpl;

import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.model.UserQuestionStat;
import com.bdr.jang.repository.UserQuestionStatRepository;
import com.bdr.jang.service.UserQuestionStatService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserQuestionStatServiceImpl implements UserQuestionStatService {

    private final UserQuestionStatRepository statRepo;
    private final EntityManager em;   // pour getReference proxy léger

    public UserQuestionStatServiceImpl(UserQuestionStatRepository statRepo,
                                       EntityManager em) {
        this.statRepo = statRepo;
        this.em       = em;
    }

    @Override
    public void recordAttempt(Long userId,
                              Integer questionId,
                              int score,
                              LocalDateTime when) {

        UserQuestionStat stat= statRepo
                .findByUserIdAndQuestionId(userId, questionId)
                .orElseGet(() -> UserQuestionStat.builder()
                        .user(em.getReference(User.class, userId))
                        .question(em.getReference(Question.class, questionId))
                        .timeSeen(0)
                        .build());

        stat.setLastScore(score);
        stat.setLastTimeSeen(when);
        stat.setTimeSeen(stat.getTimeSeen() + 1);

        statRepo.save(stat);
    }


}










