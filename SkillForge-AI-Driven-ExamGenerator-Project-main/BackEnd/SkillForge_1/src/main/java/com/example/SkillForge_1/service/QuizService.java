package com.example.SkillForge_1.service;

import com.example.SkillForge_1.dto.QuizQuestionDTO;
import com.example.SkillForge_1.dto.StudentQuizDTO;
import com.example.SkillForge_1.model.Quiz;
import com.example.SkillForge_1.model.StudentQuizAttempt;

import java.util.List;
import java.util.Map;

public interface QuizService {
    List<StudentQuizDTO> getStudentQuizzes(String email);
    List<QuizQuestionDTO> getQuestions(Long quizId);
//    public void submitQuiz(String studentEmail, Long quizId, Map<String, Integer> answers);
//void submitQuiz(String studentEmail, Long quizId, Map<String, String> answers);
    Quiz createQuiz(Quiz quiz);
    StudentQuizAttempt submitQuiz(Long quizId,String email,
                                  Map<Long, String> answers);
}
