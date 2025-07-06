package com.bdr.jang.repository;

import com.bdr.jang.entities.model.UserQuestionStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface UserQuestionStatRepository extends JpaRepository<UserQuestionStat, BigInteger> {

    Optional<UserQuestionStat> findByUserIdAndQuestionId(Long userId, Integer questionId);
}
