package com.mysite.sbb.question.service;

import com.mysite.sbb.question.dto.QuestionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @Transactional
    @Test
    void createTest() {
        for (int i = 1; i < 301; i++) {
            QuestionDto questionDto = QuestionDto.builder()
                    .content("질문 내용 " + i)
                    .subject("질문 제목 " + i)
                    .build();
//            questionService.create(questionDto);
        }
//        System.out.println("========리스트 갯수 : " + questionService.getList().size());
    }
}