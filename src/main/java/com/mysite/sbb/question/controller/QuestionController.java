package com.mysite.sbb.question.controller;

import com.mysite.sbb.answer.dto.AnswerDto;
import com.mysite.sbb.member.entity.Member;
import com.mysite.sbb.member.service.MemberService;
import com.mysite.sbb.question.dto.QuestionDto;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping(value = "/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 가능
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionService.delete(question);

        return "redirect:/question/list";
    }


    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 가능
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id,
                         QuestionDto questionDto, Principal principal){
        Question question = questionService.getQuestion(id);

        log.info("question name : {}, principal name : {}", question.getAuthor().getUsername(), principal.getName());

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        questionDto.setSubject(question.getSubject());
        questionDto.setContent(question.getContent());
        return "question/inputForm";
    }

    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 가능
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id,
                         @Valid QuestionDto questionDto,
                         BindingResult bindingResult,
                         Principal principal){
        if(bindingResult.hasErrors()){
            return "question/inputForm";
        }
        Question question = questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        questionService.modify(question, questionDto);
        return "redirect:/question/detail/" + id;
    }

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Question> paging = questionService.getList(page);
        log.info("============== paging={}", paging);
        model.addAttribute("paging", paging);
        return "question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Question question = questionService.getQuestion(id);
        log.info("=====> question : " + question);
        model.addAttribute("question", question);
        model.addAttribute("answerDto", new AnswerDto());
        return "question/detail";
    }

    @GetMapping("/create")
    public String createQuestion(QuestionDto questionDto, Model model){
        model.addAttribute("questionDto", questionDto);
        return "question/inputForm";
    }

    @PreAuthorize("isAuthenticated()") // 로그인 한 사용자만 접근 가능
    @PostMapping("/create")
    public String saveQuestion(@Valid QuestionDto questionDto,
                               BindingResult bindingResult,
                               Principal principal  ){

        if(bindingResult.hasErrors()){
            return "question/inputForm";
        }

        log.info("==== principal : " + principal.getName());
        Member member = memberService.getMember(principal.getName());

        questionService.create(questionDto, member);
        return "redirect:/question/list";
    }
}
