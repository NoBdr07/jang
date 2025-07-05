package com.bdr.jang.repository;

import com.bdr.jang.entities.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionRepository
        extends JpaRepository<Question, Integer>, JpaSpecificationExecutor<Question>, QuestionRepositoryCustom {


}
