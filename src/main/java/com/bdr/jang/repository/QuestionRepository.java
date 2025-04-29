package com.bdr.jang.repository;

import com.bdr.jang.entities.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {


}
