//package com.example.SkillForge_1.controller;
//import com.example.SkillForge_1.model.Quiz;
//import com.example.SkillForge_1.model.QuizQuestion;
//import com.example.SkillForge_1.service.AIQuizGeneratorService;
//import com.example.SkillForge_1.service.QuizService;
//import com.example.SkillForge_1.repository.QuizQuestionRepository;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/instructor")
//@CrossOrigin("*")
//public class AIQuizController {
//
//    private final AIQuizGeneratorService aiQuizGeneratorService;
//    private final QuizService quizService;
//    private final QuizQuestionRepository questionRepository;
//
//    public AIQuizController(AIQuizGeneratorService aiQuizGeneratorService,
//                            QuizService quizService,
//                            QuizQuestionRepository questionRepository) {
//        this.aiQuizGeneratorService = aiQuizGeneratorService;
//        this.quizService = quizService;
//        this.questionRepository = questionRepository;
//    }
//
//    // Create a quiz and generate AI questions
//    @PostMapping("/generate")
//    public Quiz generateQuiz(@RequestParam String quizTitle,
//                             @RequestParam Long topicId,
//                             @RequestParam Long instructorId) {
//
//        // Create Quiz object
//        Quiz quiz = new Quiz();
//        quiz.setTitle(quizTitle);
//        quiz.setTopicId(topicId);
//        quiz.setCreatedBy(null); // set instructor object if needed
//        quiz.setDifficulty("EASY"); // default, can pass as param
//        quiz.setActive(true);
//
//        Quiz savedQuiz = quizService.createQuiz(quiz);
//
//        // Generate AI questions
//        List<QuizQuestion> questions = aiQuizGeneratorService.generateQuestions(savedQuiz);
//
//        // Save questions
//        questionRepository.saveAll(questions);
//
//        return savedQuiz;
//    }
//
//    // Fetch all questions of a quiz
//    @GetMapping("/{quizId}/questions")
//    public List<QuizQuestion> getQuizQuestions(@PathVariable Long quizId) {
//        return questionRepository.findByQuiz_QuizId(quizId);
//    }
//}
package com.example.SkillForge_1.controller;
import com.example.SkillForge_1.dto.AIRequestDTO;
import com.example.SkillForge_1.dto.QuizRequestDTO;
import com.example.SkillForge_1.model.Quiz;
import com.example.SkillForge_1.model.QuizQuestion;
import com.example.SkillForge_1.service.AIQuizGeneratorService;
import com.example.SkillForge_1.service.QuizService;
import com.example.SkillForge_1.repository.QuizQuestionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructor")
@CrossOrigin("*")
public class AIQuizController {
    private final AIQuizGeneratorService aiQuizGeneratorService;
    private final QuizService quizService;
    private final QuizQuestionRepository questionRepository;

    public AIQuizController(AIQuizGeneratorService aiQuizGeneratorService,
                            QuizService quizService,
                            QuizQuestionRepository questionRepository) {
        this.aiQuizGeneratorService = aiQuizGeneratorService;
        this.quizService = quizService;
        this.questionRepository = questionRepository;
    }
//
//    // Create a quiz and generate AI questions
//    @PostMapping("/generate")
//    public Quiz generateQuiz(@RequestBody QuizRequestDTO request) {
//
//        // Create Quiz object
//        Quiz quiz = new Quiz();
//        quiz.setTitle(request.getQuizTitle());
//        quiz.setTopicId(request.getTopicId());
//        quiz.setCreatedBy(null); // TODO: set instructor object by instructorId if needed
//        quiz.setDifficulty("EASY"); // default
//        quiz.setActive(true);
//
//        // Save quiz
//        Quiz savedQuiz = quizService.createQuiz(quiz);
//
//        // Generate AI questions
//        List<QuizQuestion> questions = aiQuizGeneratorService.generateQuestions(savedQuiz);
//
//        // Save questions
//        questionRepository.saveAll(questions);
//
//        return savedQuiz;
//    }
//@PostMapping("/generate")
//public List<QuizQuestion> generateQuiz(@RequestBody AIRequestDTO request) {
//
//    Quiz quiz = new Quiz();
//    quiz.setTitle(request.getTopicName() + " Quiz");
//    quiz.setDifficulty(request.getDifficulty());
//    quiz.setActive(true);
//
//    Quiz savedQuiz = quizService.createQuiz(quiz);
//
//    List<QuizQuestion> questions =
//            aiQuizGeneratorService.generateQuestions(savedQuiz);
//
//    questionRepository.saveAll(questions);
//
//    return questions; // ✅ RETURN ARRAY
//}
@PostMapping("/generate")
public List<QuizQuestion> generateQuiz(@RequestBody AIRequestDTO request) {

    // DO NOT create quiz here
    Quiz tempQuiz = new Quiz();
    tempQuiz.setTitle(request.getTopicName());
    tempQuiz.setDifficulty(request.getDifficulty());
    tempQuiz.setQuestionType(request.getQuestionType());

    // Generate AI questions only
    return aiQuizGeneratorService.generateQuestions(tempQuiz);
}



    // Fetch all questions of a quiz
    @GetMapping("/{quizId}/questions")
    public List<QuizQuestion> getQuizQuestions(@PathVariable Long quizId) {
        return questionRepository.findByQuiz_QuizId(quizId);
    }
}
