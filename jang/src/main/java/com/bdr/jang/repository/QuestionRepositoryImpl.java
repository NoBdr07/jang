package com.bdr.jang.repository;

import com.bdr.jang.entities.model.Question;
import com.bdr.jang.entities.model.Topic;
import com.bdr.jang.entities.model.UserQuestionStat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final EntityManager em;

    public QuestionRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Candidate> findCandidatesWithStats(
            List<Integer> niveaux,
            List<String> topics,
            Long userId,
            Pageable pageable
    ) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();

        // racine Question
        Root<Question> q = cq.from(Question.class);

        // join vers UserQuestionStat limité à l'utilisateur courant
        Join<Question, UserQuestionStat> stat = q.join(
                "userQuestionStats", JoinType.LEFT);
        stat.on(cb.equal(stat.get("user").get("id"), userId));

        // projection : question + stat
        cq.multiselect(q, stat);

        // filtres dynamiques
        List<Predicate> where = new ArrayList<>();

        if(niveaux != null && !niveaux.isEmpty()) {
            where.add(q.get("level").in(niveaux));
        }

        if(topics != null && !topics.isEmpty()) {
            Join<Question, Topic> t = q.join("topic", JoinType.INNER);
            where.add(t.get("name").in(topics));
        }

        if(!where.isEmpty()) {
            cq.where(where.toArray(Predicate[]::new));

        }

        // exécution + pagination du pool (appel de plus de questions que nécessaire)
        TypedQuery<Tuple> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Tuple> tuples = query.getResultList(); // execute la requete

        // mapping tuple -> candidate
        return tuples.stream()
                .map(t -> new Candidate(
                        t.get(0, Question.class),
                        t.get(1, UserQuestionStat.class)
                        )).toList();

    }
}















