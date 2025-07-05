package com.bdr.jang.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionRepositoryCustom {
    List<Candidate> findCandidatesWithStats(
            List<Integer> niveaux,
            List<String> topics,
            Long userId,
            Pageable pageable
    );

}
