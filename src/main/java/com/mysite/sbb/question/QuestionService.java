package com.mysite.sbb.question;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 목록 조회 + 검색 기능
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    // 단일 질문 조회
    public Question getQuestion(Integer id) {
        return this.questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    // 질문 생성 (글쓴이 포함)
    public void create(String subject, String content, SiteUser author) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(author);
        this.questionRepository.save(question);
    }

    // 질문 수정
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    // 질문 삭제
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    // 질문 추천
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    // 검색 조건(Specification) 생성 (제목/내용/질문작성자/답변내용/답변작성자)
    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);   // 중복 제거

                // question.author (질문 작성자)
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                // question.answerList (답변들)
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                // answer.author (답변 작성자)
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);

                String likePattern = "%" + kw + "%";

                return cb.or(
                        cb.like(q.get("subject"), likePattern),      // 질문 제목
                        cb.like(q.get("content"), likePattern),      // 질문 내용
                        cb.like(u1.get("username"), likePattern),    // 질문 작성자
                        cb.like(a.get("content"), likePattern),      // 답변 내용
                        cb.like(u2.get("username"), likePattern)     // 답변 작성자
                );
            }
        };
    }
}
