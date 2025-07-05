package com.bdr.jang.controller;

import com.bdr.jang.entities.dto.QuestionDTO;
import com.bdr.jang.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.security.Principal;
import java.util.List;

/**
 * REST controller for CRUD operations on questions
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Retrieves a page of x questions in all questions
     *
     * @param pageable
     * @return 200 with all questions if succeed
     */
    @GetMapping
    public ResponseEntity<Page<QuestionDTO>> getAllQuestions(
            @PageableDefault(page = 0, size = 10, sort = "id")
            Pageable pageable
    ) {
        Page<QuestionDTO> questions = questionService.getAllQuestions(pageable);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/id/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable("questionId") Integer id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    // récuperer questions suivant sujet/niveaux, aléatoire ou non aléatoire
    @GetMapping("/filter")
    public ResponseEntity<Page<QuestionDTO>> getFilteredQuestions(
            @RequestParam(required = false) List<Integer> niveaux,
            @RequestParam MultiValueMap<String, String> params,
            @RequestParam(defaultValue = "false") boolean random,
            Pageable pageable
    ) {
        List<String> topics = params.getOrDefault("topics", List.of());

        Page<QuestionDTO> page = questionService.getQuestionsByFilter(niveaux, topics, pageable, random);
        return ResponseEntity.ok(page);
    }

    // récupérer questions quand connecté, en fonction de notre progression
    @GetMapping("/adaptive")
    public ResponseEntity<Page<QuestionDTO>> getAdaptiveQuestions(
            @RequestParam(required = false) List<Integer> niveaux,
            @RequestParam(required = false) List<String> topics,
            @RequestParam(defaultValue = "10")               int size,
            Principal principal
    ) {
        // 1. Qui est l’utilisateur ?
        Long userId = (principal == null)
                ? null
                : Long.valueOf(principal.getName());

        // 2. Appel du service : pas besoin de "page", on renvoie toujours 1 page de 'size'
        Page<QuestionDTO> page = questionService.getAdaptiveQuestions(
                niveaux, topics, userId, size);

        return ResponseEntity.ok(page);
    }

    @PostMapping()
    public ResponseEntity<QuestionDTO> addNewQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        QuestionDTO createdQuestionDto = questionService.createQuestion(questionDTO);
        return ResponseEntity.ok(createdQuestionDto);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Integer questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok("Question deleted");
    }

    /**
     * Met à jour une question existante (titre, réponse, niveau, topic).
     *
     * @param questionId  l’ID de la question à modifier
     * @param questionDTO le DTO contenant les nouveaux champs (title, answer, level, topicName)
     * @return 200 + QuestionDTO mis à jour, ou 404 si l’ID n’existe pas
     */
    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Integer questionId,
            @Valid @RequestBody QuestionDTO questionDTO
    ) {
        // On force le DTO à avoir l’ID de la ressource à mettre à jour
        questionDTO.setId(questionId);
        QuestionDTO updated = questionService.updateQuestion(questionDTO);
        return ResponseEntity.ok(updated);
    }


}
