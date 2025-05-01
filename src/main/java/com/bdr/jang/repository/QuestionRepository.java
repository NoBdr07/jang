package com.bdr.jang.repository;

import com.bdr.jang.entities.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.concurrent.Executor;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Executor> {

    @Override
    Page<Question> findAll(Pageable pageable);



}
