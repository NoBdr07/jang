package com.bdr.jang.entities.specification;

import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class QuestionSpecs {

    public static Specification<Question> hasLevel(Integer niveau) {
        return (root, query, cb) ->
                (niveau == null)
                        ? cb.conjunction()
                        : cb.equal(root.get("level"), niveau);
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
}
