//
//package com.example.SkillForge_1.service;
//
//import com.example.SkillForge_1.dto.*;
//import com.example.SkillForge_1.model.*;
//import com.example.SkillForge_1.repository.*;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional(readOnly = true)
//public class QuizAnalyticsService {
//
//    private final QuizRepository quizRepository;
//    private final QuizQuestionRepository questionRepository;
//    private final StudentQuizAssignmentRepository assignmentRepository;
//    private final StudentQuizAttemptRepository attemptRepository;
//    private final StudentRepository studentRepository;
//
//    public QuizAnalyticsService(
//            QuizRepository quizRepository,
//            QuizQuestionRepository questionRepository,
//            StudentQuizAssignmentRepository assignmentRepository,
//            StudentQuizAttemptRepository attemptRepository,
//            StudentRepository studentRepository
//    ) {
//        this.quizRepository = quizRepository;
//        this.questionRepository = questionRepository;
//        this.assignmentRepository = assignmentRepository;
//        this.attemptRepository = attemptRepository;
//        this.studentRepository = studentRepository;
//    }
//
//    // =====================================================
//    // QUIZ ANALYTICS (Instructor Dashboard)
//    // =====================================================
//    public QuizAnalyticsDTO getQuizAnalytics(Long quizId) {
//
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        // ---------------- QUESTIONS ----------------
//        List<QuizQuestionDTO> questions =
//                questionRepository.findByQuiz_QuizId(quizId)
//                        .stream()
//                        .map(q -> {
//                            QuizQuestionDTO dto = new QuizQuestionDTO();
//                            dto.setQuestionId(q.getQuestionId());
//                            dto.setQuestionText(q.getQuestion());
//                            dto.setOptions(List.of(
//                                    q.getOptionA(),
//                                    q.getOptionB(),
//                                    q.getOptionC(),
//                                    q.getOptionD()
//                            ));
//                            dto.setCorrectAnswer(q.getCorrectAnswer());
//                            return dto;
//                        })
//                        .collect(Collectors.toList());
//
//        // ---------------- COUNTS ----------------
//        int totalAssigned = assignmentRepository.countByQuiz_QuizId(quizId);
//        int totalAttempted = attemptRepository.countByQuiz_QuizId(quizId);
//
//        QuizAnalyticsDTO dto = new QuizAnalyticsDTO();
//        dto.setQuizId(quiz.getQuizId());
//        dto.setTitle(quiz.getTitle());
//        dto.setDifficulty(quiz.getDifficulty());
//        dto.setTotalMarks(quiz.getTotalMarks());
//        dto.setQuestions(questions);
//        dto.setTotalAssigned(totalAssigned);
//        dto.setTotalAttempted(totalAttempted);
//
//        return dto;
//    }
//
//    // =====================================================
//    // STUDENT ATTEMPTS PER QUIZ
//    // =====================================================
//    public List<StudentAttemptDTO> getQuizAttempts(Long quizId) {
//
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        List<StudentQuizAssignment> assignments =
//                assignmentRepository.findByQuiz_QuizId(quizId);
//
//        return assignments.stream().map(assignment -> {
//
//            Long studentId = assignment.getStudent().getId();
//
//            StudentAttemptDTO dto = new StudentAttemptDTO();
//            dto.setStudentId(studentId);
//
//            Optional<Student> studentOpt = studentRepository.findById(studentId);
//            dto.setStudentName(
//                    studentOpt.map(s -> s.getUser().getName())
//                            .orElse("Student " + studentId)
//            );
//
//            boolean attempted =
//                    attemptRepository.existsByStudent_IdAndQuiz_QuizId(studentId, quizId);
//
//            dto.setCompleted(attempted);
//
//            if (attempted) {
//                Optional<StudentQuizAttempt> attempt =
//                        attemptRepository.findAll()
//                                .stream()
//                                .filter(a ->
//                                        a.getStudent().getId().equals(studentId) &&
//                                                a.getQuiz().getQuizId().equals(quizId))
//                                .findFirst();
//            } else {
//                dto.setScore(0);
//            }
//
//            return dto;
//        }).collect(Collectors.toList());
//    }
//
//    // =====================================================
//    // QUIZ DETAILS (Edit / Review Page)
//    // =====================================================
//    public QuizAnalyticsDTO getQuizDetails(Long quizId) {
//
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        List<QuizQuestionDTO> questions =
//                questionRepository.findByQuiz_QuizId(quizId)
//                        .stream()
//                        .map(q -> {
//                            QuizQuestionDTO dto = new QuizQuestionDTO();
//                            dto.setQuestionId(q.getQuestionId());
//                            dto.setQuestionText(q.getQuestion());
//                            dto.setOptions(List.of(
//                                    q.getOptionA(),
//                                    q.getOptionB(),
//                                    q.getOptionC(),
//                                    q.getOptionD()
//                            ));
//                            dto.setCorrectAnswer(q.getCorrectAnswer());
//                            return dto;
//                        })
//                        .collect(Collectors.toList());
//
//        QuizAnalyticsDTO dto = new QuizAnalyticsDTO();
//        dto.setQuizId(quiz.getQuizId());
//        dto.setTitle(quiz.getTitle());
//        dto.setDifficulty(quiz.getDifficulty());
//        dto.setTotalMarks(quiz.getTotalMarks());
//        dto.setQuestions(questions);
//
//        return dto;
//    }
//}
package com.example.SkillForge_1.service;

import com.example.SkillForge_1.dto.*;
import com.example.SkillForge_1.model.*;
import com.example.SkillForge_1.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuizAnalyticsService {

    private final QuizRepository quizRepository;
    private final StudentQuizAssignmentRepository assignmentRepository;
    private final StudentQuizAttemptRepository attemptRepository;
    private final StudentRepository studentRepository;
    private final QuizQuestionRepository questionRepository;

    public QuizAnalyticsService(
            QuizRepository quizRepository,
            StudentQuizAssignmentRepository assignmentRepository,
            StudentQuizAttemptRepository attemptRepository,
            StudentRepository studentRepository,
            QuizQuestionRepository questionRepository
    ) {
        this.quizRepository = quizRepository;
        this.assignmentRepository = assignmentRepository;
        this.attemptRepository = attemptRepository;
        this.studentRepository = studentRepository;
        this.questionRepository = questionRepository;
    }

    // =====================================================
    // QUIZ ANALYTICS (Instructor Dashboard)
    // =====================================================
    public QuizAnalyticsDTO getQuizAnalytics(Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        // ---------------- QUESTIONS ----------------
        List<QuizQuestionDTO> questionDTOs =
                questionRepository.findByQuiz_QuizId(quizId)
                        .stream()
                        .map(q -> {
                            QuizQuestionDTO dto = new QuizQuestionDTO();
                            dto.setQuestionId(q.getQuestionId());
                            dto.setQuestionText(q.getQuestion());
                            dto.setOptions(List.of(
                                    q.getOptionA(),
                                    q.getOptionB(),
                                    q.getOptionC(),
                                    q.getOptionD()
                            ));
                            dto.setCorrectAnswer(q.getCorrectAnswer());
                            return dto;
                        })
                        .collect(Collectors.toList());

        // ---------------- ASSIGNED STUDENTS ----------------
        List<AssignedStudentDTO> assignedStudents = assignmentRepository.findByQuiz_QuizId(quizId)
                .stream()
                .map(a -> {
                    Student student = a.getStudent();
                    String name = student.getUser() != null ? student.getUser().getName() : "Unknown";
                    return new AssignedStudentDTO(student.getId(), name);
                })
                .collect(Collectors.toList());

        // ---------------- ATTEMPTS ----------------
        List<QuizAttemptAnalyticsDTO> attempts = attemptRepository.findByQuiz_QuizId(quizId)
                .stream()
                .filter(a -> a.getStudent() != null)
                .map(a -> {
                    try {
                        UserAuthentication user = a.getStudent();
                        Student student = studentRepository.findByUser_Id(user.getId())
                                .orElse(null);
                        
                        if (student == null) {
                            return null;
                        }
                        
                        String name = user.getName() != null ? user.getName() : "Unknown";
                        String evalStatus = a.getStatus() != null ? a.getStatus().name() : "COMPLETED";
                        
                        // Calculate total score from all components
                        Integer autoScore = a.getAutoScore() != null ? a.getAutoScore() : 0;
                        Integer manualScore = a.getManualScore() != null ? a.getManualScore() : 0;
                        Integer aiScore = a.getAiScore() != null ? a.getAiScore() : 0;
                        Integer totalScore = autoScore + manualScore + aiScore;
                        
                        // Use the stored score if available, otherwise use calculated total
                        Integer finalScore = a.getScore() != null ? a.getScore() : totalScore;
                        
                        return new QuizAttemptAnalyticsDTO(
                            a.getAttemptId(),
                            student.getId(),
                            name,
                            finalScore,
                            evalStatus,
                            a.getSubmittedAt()
                        );
                    } catch (Exception e) {
                        System.err.println("Error processing attempt: " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        // ---------------- BUILD DTO ----------------
        QuizAnalyticsDTO dto = new QuizAnalyticsDTO();
        dto.setQuizId(quiz.getQuizId());
        dto.setTitle(quiz.getTitle());
        dto.setDifficulty(quiz.getDifficulty());
        dto.setTotalMarks(quiz.getTotalMarks());
        dto.setQuestions(questionDTOs);
        dto.setAssignedStudents(assignedStudents);
        dto.setAttempts(attempts);
        dto.setTotalAssigned(assignedStudents.size());
        dto.setTotalAttempted(attempts.size());

        return dto;
    }

    // =====================================================
    // QUIZ DETAILS (for Review / Edit)
    // =====================================================
    public QuizAnalyticsDTO getQuizDetails(Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        List<QuizQuestionDTO> questions =
                questionRepository.findByQuiz_QuizId(quizId)
                        .stream()
                        .map(q -> {
                            QuizQuestionDTO dto = new QuizQuestionDTO();
                            dto.setQuestionId(q.getQuestionId());
                            dto.setQuestionText(q.getQuestion());
                            dto.setOptions(List.of(
                                    q.getOptionA(),
                                    q.getOptionB(),
                                    q.getOptionC(),
                                    q.getOptionD()
                            ));
                            dto.setCorrectAnswer(q.getCorrectAnswer());
                            return dto;
                        })
                        .collect(Collectors.toList());

        QuizAnalyticsDTO dto = new QuizAnalyticsDTO();
        dto.setQuizId(quiz.getQuizId());
        dto.setTitle(quiz.getTitle());
        dto.setDifficulty(quiz.getDifficulty());
        dto.setTotalMarks(quiz.getTotalMarks());
        dto.setQuestions(questions);

        return dto;
    }
}
