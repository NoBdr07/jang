package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.UserQuestionStatDTO;
import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.User;
import com.bdr.jang.entities.model.UserQuestionStat;
import com.bdr.jang.service.QuestionService;
import com.bdr.jang.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserQuestionStatMapper {

    private final UserService userService;
    private final QuestionService questionService;

    public UserQuestionStatMapper(UserService userService,
                                  QuestionService questionService) {
        this.userService     = userService;
        this.questionService = questionService;
    }

    /** Convertit l’entité en DTO pour l’api */
    public UserQuestionStatDTO mapToDTO(UserQuestionStat entity) {
        return UserQuestionStatDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .questionId(entity.getQuestion().getId())
                .lastTimeSeen(entity.getLastTimeSeen())
                .lastScore(entity.getLastScore())
                .timeSeen(entity.getTimeSeen())
                .build();
    }

    /** Convertit le DTO en entité en récupérant User/Question via vos services */
    public UserQuestionStat mapToEntity(UserQuestionStatDTO dto) {
        User user = userService.findUserEntityById(dto.getUserId());

        Question question = questionService.getQuestionEntityById(dto.getQuestionId());

        return UserQuestionStat.builder()
                .id(dto.getId())
                .user(user)
                .question(question)
                .lastTimeSeen(dto.getLastTimeSeen())
                .lastScore(dto.getLastScore())
                .timeSeen(dto.getTimeSeen())
                .build();
    }
}
