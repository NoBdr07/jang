package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.UserQuestionStatDTO;
import com.bdr.jang.entities.model.UserQuestionStat;
import org.springframework.stereotype.Component;

@Component
public class UserQuestionStatMapper {

    public UserQuestionStatDTO mapToDTO(UserQuestionStat e) {
        return UserQuestionStatDTO.builder()
                .id(e.getId())
                .lastTimeSeen(e.getLastTimeSeen())
                .lastScore(e.getLastScore())
                .timeSeen(e.getTimeSeen())
                .build();
    }

    public UserQuestionStat mapToEntity(UserQuestionStatDTO dto) {
        return UserQuestionStat.builder()
                .id(dto.getId())
                .lastTimeSeen(dto.getLastTimeSeen())
                .lastScore(dto.getLastScore())
                .timeSeen(dto.getTimeSeen())
                .build();
    }
}
