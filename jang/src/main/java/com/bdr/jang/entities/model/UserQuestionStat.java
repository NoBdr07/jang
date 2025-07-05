package com.bdr.jang.entities.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_question_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQuestionStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime lastTimeSeen;

    @Column(nullable = false)
    private Integer lastScore;

    @Column(nullable = false)
    private Integer timeSeen;
}
