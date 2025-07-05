package com.bdr.jang.entities.mapper;

import com.bdr.jang.entities.dto.AttemptDTO;
import com.bdr.jang.entities.model.Attempt;
import org.springframework.stereotype.Component;

@Component
public class AttemptMapper {

    public AttemptDTO mapToDTO(Attempt e) {
        return AttemptDTO.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .questionId(e.getQuestionId())
                .score(e.getScore())
                .answeredAt(e.getAnsweredAt())
                .build();
    }

    public Attempt mapToEntity(AttemptDTO dto) {
        return Attempt.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .questionId(dto.getQuestionId())
                .score(dto.getScore())
                .answeredAt(dto.getAnsweredAt())
                .build();
    }
}
