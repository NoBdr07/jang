package com.bdr.jang.controller;

import com.bdr.jang.entities.payload.AttemptLight;
import com.bdr.jang.service.AttemptService;
import com.bdr.jang.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttemptController {

    private final AttemptService attemptService;
    private final UserService userService;

    public AttemptController(AttemptService attemptService,
                             UserService userService) {
        this.attemptService = attemptService;
        this.userService = userService;
    }

    @PostMapping("/series-results")
    public ResponseEntity<Void> recordSeries(@RequestBody @Valid List<AttemptLight> attemps, Principal principal) {

        Long userId = toUserId(principal);

        attemptService.recordSeries(userId, attemps);
        return ResponseEntity.ok().build();
    }

    /* -------- helper interne -------- */
    private Long toUserId(Principal principal) {
        return (principal == null)
                ? null
                : userService.getUserIdByUsername(principal.getName());
    }
}
