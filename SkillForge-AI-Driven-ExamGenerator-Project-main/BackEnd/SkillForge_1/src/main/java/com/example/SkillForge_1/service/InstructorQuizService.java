
package com.example.SkillForge_1.service;

import com.example.SkillForge_1.dto.QuizCreateDTO;
import com.example.SkillForge_1.dto.QuizQuestionDTO;
import com.example.SkillForge_1.dto.StudentDTO;
import com.example.SkillForge_1.model.*;
import com.example.SkillForge_1.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorQuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private QuizQuestionRepository questionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentQuizAssignmentRepository assignmentRepository;

    @Autowired
    private StudentQuestionResponseRepository responseRepo;

    @Autowired
    private StudentQuizAttemptRepository attemptRepository;

    // ================= CREATE QUIZ =================
    @Transactional
    public Quiz createQuiz(QuizCreateDTO dto) {

        if (dto.getQuestions() == null || dto.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("Quiz must have at least one question");
        }

        if (dto.getAssignedStudentIds() == null || dto.getAssignedStudentIds().isEmpty()) {
            throw new IllegalArgumentException("Quiz must be assigned to students");
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setTopicId(dto.getTopicId());
        quiz.setDifficulty(dto.getDifficulty());
        quiz.setTotalMarks(dto.getTotalMarks());
        quiz = quizRepository.save(quiz);

        // Create questions
        List<QuizQuestion> questions = new ArrayList<>();
        for (QuizQuestionDTO qdto : dto.getQuestions()) {

            QuizQuestion q = new QuizQuestion();
            q.setQuiz(quiz);
            q.setQuestion(qdto.getQuestionText());
            q.setType(qdto.getQuestionType());

            if ("MCQ".equalsIgnoreCase(qdto.getQuestionType())) {

                List<String> opts = qdto.getOptions();
                if (opts == null || opts.size() < 2) {
                    throw new IllegalArgumentException("MCQ must have options");
                }

                q.setOptionA(opts.size() > 0 ? opts.get(0) : null);
                q.setOptionB(opts.size() > 1 ? opts.get(1) : null);
                q.setOptionC(opts.size() > 2 ? opts.get(2) : null);
                q.setOptionD(opts.size() > 3 ? opts.get(3) : null);
                q.setCorrectAnswer(qdto.getCorrectAnswer());

            } else {
                q.setOptionA(null);
                q.setOptionB(null);
                q.setOptionC(null);
                q.setOptionD(null);
                q.setCorrectAnswer("");
            }

            questions.add(q);
        }

        questionRepository.saveAll(questions);
        quiz.setQuestions(questions);
        // Assign students
        List<Student> students = studentRepository.findAllById(dto.getAssignedStudentIds());
        List<StudentQuizAssignment> assignments = new ArrayList<>();
        for (Student s : students) {
            StudentQuizAssignment a = new StudentQuizAssignment();
            a.setQuiz(quiz);
            a.setStudent(s);
            assignments.add(a);
        }
        assignmentRepository.saveAll(assignments);
        quiz.setQuestions(questions);
        return quiz;
    }

    // ================= GET ALL STUDENTS =================
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> dtos = new ArrayList<>();
        for (Student s : students) {
            dtos.add(new StudentDTO(
                    s.getId(),
                    s.getUser().getName(),
                    s.getUser().getId()
            ));
        }
        return dtos;
    }

    // ================= UPDATE QUIZ =================
    @Transactional
    public Quiz updateQuiz(Long quizId, QuizCreateDTO dto) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(dto.getTitle());
        quiz.setDifficulty(dto.getDifficulty());
        quiz.setTotalMarks(dto.getTotalMarks());
        quiz.setTopicId(dto.getTopicId());
        quizRepository.save(quiz);

        questionRepository.deleteByQuiz_QuizId(quizId);

        List<QuizQuestion> questions = new ArrayList<>();
        for (QuizQuestionDTO qdto : dto.getQuestions()) {

            QuizQuestion q = new QuizQuestion();
            q.setQuiz(quiz);
            q.setQuestion(qdto.getQuestionText());
            q.setType(qdto.getQuestionType());

            if ("MCQ".equalsIgnoreCase(qdto.getQuestionType())) {
                List<String> opts = qdto.getOptions();
                q.setOptionA(opts.size() > 0 ? opts.get(0) : null);
                q.setOptionB(opts.size() > 1 ? opts.get(1) : null);
                q.setOptionC(opts.size() > 2 ? opts.get(2) : null);
                q.setOptionD(opts.size() > 3 ? opts.get(3) : null);
                q.setCorrectAnswer(qdto.getCorrectAnswer());
            } else {
                q.setOptionA(null);
                q.setOptionB(null);
                q.setOptionC(null);
                q.setOptionD(null);
                q.setCorrectAnswer("");
            }

            questions.add(q);
        }

        questionRepository.saveAll(questions);
        return quiz;
    }

    // ================= DELETE QUIZ =================
    @Transactional
    public void deleteQuiz(Long quizId) {
        // Delete in correct order: responses -> attempts -> assignments -> questions -> quiz
        List<StudentQuizAttempt> attempts = attemptRepository.findByQuiz_QuizId(quizId);
        for (StudentQuizAttempt attempt : attempts) {
            responseRepo.deleteAll(responseRepo.findByAttempt_AttemptId(attempt.getAttemptId()));
        }
        attemptRepository.deleteAll(attempts);
        assignmentRepository.deleteByQuiz_QuizId(quizId);
        questionRepository.deleteByQuiz_QuizId(quizId);
        quizRepository.deleteById(quizId);
    }

    // ================= MANUAL EVALUATION =================
    @Transactional
    public void evaluateLongAnswer(Long responseId, Integer marks) {

        StudentQuestionResponse resp = responseRepo.findById(responseId)
                .orElseThrow();

        resp.setMarksObtained(marks);
        resp.setStatus(QuestionEvaluationStatus.MANUALLY_EVALUATED);
        responseRepo.save(resp);

        StudentQuizAttempt attempt = resp.getAttempt();

        attempt.setManualScore(
                (attempt.getManualScore() == null ? 0 : attempt.getManualScore()) + marks
        );

        attempt.setScore(
                attempt.getAutoScore()
                        + attempt.getManualScore()
                        + (attempt.getAiScore() == null ? 0 : attempt.getAiScore())
        );

        attempt.setStatus(AttemptStatus.COMPLETED);
        attemptRepository.save(attempt);
    }
}
