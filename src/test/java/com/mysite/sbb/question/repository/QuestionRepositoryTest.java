package com.mysite.sbb.question.repository;

import com.mysite.sbb.question.entity.Question;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void testSave(){
        Question question = new Question();
        question.setSubject("SBB가 뭔가요?");
        question.setContent("sbb에 대해 알고 싶어요.");
        System.out.println("(B)question :" + question);
        Question saveQ = questionRepository.save(question);
        System.out.println("(A)question :" + saveQ);

        Question q2 = Question.builder()
                .content("질문 내용 입력")
                .subject("질문 주제 입력")
                .build();
        System.out.println("(B)q2 :" + q2);
        Question sQ2 = questionRepository.save(q2);
        System.out.println("(A)q2 :" + sQ2);
    }

    @Test
    public void testFindAll(){
        List<Question> questionList = questionRepository.findAll();
        System.out.println("List :" + questionList);
        assertEquals(2, questionList.size());
    }

    @Test
    public void testLike(){
        Question qq = questionRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("해당 엔티티 존재X"));

//        Optional<Question> q1 = questionRepository.findBySubjectLike("%sbb%");
//        if(q1.isPresent()){
//            Question q = q1.get();
//            assertEquals("abc가 뭔가요?", q.getSubject());
//        } else {
//            throw new EntityNotFoundException("해당 엔티티가 존재 X");
//        }
    }
}