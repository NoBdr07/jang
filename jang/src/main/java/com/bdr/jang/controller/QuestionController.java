package com.bdr.jang.controller;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public ResponseEntity<Page<QuestionDTO>> getAllQuestions(
            @PageableDefault(page = 0, size = 20, sort = "id")
            Pageable pageable
    ) {
        Page<QuestionDTO> questions = questionService.getAllQuestions(pageable);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/id/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable("questionId") Long id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<QuestionDTO>> getFilteredQuestions(
            @RequestParam(required = false) Integer niveau,
            @RequestParam(required = false) List<String> topics,
            Pageable pageable
    ) {
        Page<QuestionDTO> filteredQuestions = questionService.getQuestionsByFilter(niveau, topics, pageable);
        return ResponseEntity.ok(filteredQuestions);
    }


}
