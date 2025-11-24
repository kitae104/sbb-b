package com.mysite.sbb.answer.controller;

import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.service.AnswerService;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import com.mysite.sbb.question.dto.QuestionDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;

    private final AnswerService answerService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 가능
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        log.info("answer name : {}, principal name : {}", answer.getAuthor().getUsername(), principal.getName());

        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }


        answerService.delete(answer);

        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }



    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 가능
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id,
                         @ModelAttribute("answerDto") AnswerDto answerDto, Principal principal){
        Answer answer = answerService.getAnswer(id);

        log.info("answer name : {}, principal name : {}", answer.getAuthor().getUsername(), principal.getName());

        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerDto.setContent(answer.getContent());
//        model.addAttribute("answerDto", answerDto);

        return "answer/modifyForm";
    }

    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 가능
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id,
                         @Valid AnswerDto answerDto,
                         BindingResult bindingResult,
                         Principal principal){

        if(bindingResult.hasErrors()){
            return "answer/modifyForm";
        }

        Answer answer = answerService.getAnswer(id);

        log.info("answer name : {}, principal name : {}", answer.getAuthor().getUsername(), principal.getName());

        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        answerService.modify(answer, answerDto);

        return "redirect:/question/detail/" + answer.getQuestion().getId();
    }


    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 접근 가능
    @PostMapping("/create/{id}")
    public String create(@PathVariable("id") Long id,
                         @Valid AnswerDto answerDto,
                         BindingResult bindingResult,
                         Principal principal, // 현재 로그인된 사용자
                         Model model) {
        Question question = questionService.getQuestion(id);

        if(bindingResult.hasErrors()){
            model.addAttribute("question",question);
            model.addAttribute("answerDto", answerDto);
            return "detail_";
        }

        Member member = memberService.getMember(principal.getName());

        answerService.create(question, answerDto, member);

        return "redirect:/question/detail/" + id;
    }
}
