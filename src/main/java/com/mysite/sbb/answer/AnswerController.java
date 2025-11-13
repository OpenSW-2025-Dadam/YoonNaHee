package com.mysite.sbb.answer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable("id") Integer id,
                               @RequestParam("content") String content) {
        Question q = this.questionService.getQuestion(id);
        answerService.create(q, content);
        return "redirect:/question/detail/" + id;
    }
}
