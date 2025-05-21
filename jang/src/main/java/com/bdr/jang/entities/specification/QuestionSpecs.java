package com.bdr.jang.entities.specification;

import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class QuestionSpecs {

    public static Specification<Question> hasLevels(List<Integer> niveaux) {
        return (root, query, cb) -> {
            if (niveaux == null || niveaux.isEmpty()) {
                // pas de filtre si aucune sélection
                return cb.conjunction();
            }
            // WHERE level IN (:niveaux)
            return root.get("level").in(niveaux);
        };
    }

    public static Specification<Question> hasTopics(List<String> topicNames) {
        return (root, query, cb) -> {
            if (topicNames == null || topicNames.isEmpty()) {
                return cb.conjunction();
            }
            Join<Question, Topic> joinTopics = root.join("topic", JoinType.INNER);
            return joinTopics.get("name").in(topicNames);
        };
    }

    public static Specification<Question> randomOrder() {
        return (root, query, cb) -> {
            if (Long.class != query.getResultType()) {
                query.orderBy(cb.asc(cb.function("RAND", Double.class)));
            }
            return cb.conjunction();
        };
    }
}
