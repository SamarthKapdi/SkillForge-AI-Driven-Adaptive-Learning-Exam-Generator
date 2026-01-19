package com.example.SkillForge_1.controller;

import com.example.SkillForge_1.dto.QuizAnalyticsDTO;
import com.example.SkillForge_1.service.QuizAnalyticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizAnalyticsService quizAnalyticsService;

    public QuizController(QuizAnalyticsService quizAnalyticsService) {
        this.quizAnalyticsService = quizAnalyticsService;
    }

    // 🔹 FETCH QUIZ DETAILS FOR VIEW / EDIT
    @GetMapping("/{quizId}/details")
    public QuizAnalyticsDTO getQuizDetails(@PathVariable Long quizId) {
        return quizAnalyticsService.getQuizAnalytics(quizId);
    }
}
