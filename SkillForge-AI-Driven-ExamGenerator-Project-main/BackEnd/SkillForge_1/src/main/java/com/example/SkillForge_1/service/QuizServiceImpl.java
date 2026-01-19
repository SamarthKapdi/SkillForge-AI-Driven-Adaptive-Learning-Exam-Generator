
package com.example.SkillForge_1.service;

import com.example.SkillForge_1.dto.QuizQuestionDTO;
import com.example.SkillForge_1.dto.StudentQuizDTO;
import com.example.SkillForge_1.model.*;
import com.example.SkillForge_1.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuizServiceImpl implements QuizService{

    @Autowired
    private UserAuthenticationRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private StudentQuizAssignmentRepository assignmentRepository;

    @Autowired
    private StudentQuizAttemptRepository attemptRepository;

    @Autowired
    private StudentQuestionResponseRepository studentQuestionResponseRepository;

    // ================= STUDENT QUIZZES =================
    @Override
    public List<StudentQuizDTO> getStudentQuizzes(String email) {
        UserAuthentication user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = studentRepository.findByUser_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));

        List<StudentQuizAssignment> assignments =
                assignmentRepository.findByStudent_Id(student.getId());

        List<StudentQuizDTO> result = new ArrayList<>();
        for (StudentQuizAssignment a : assignments) {
            Quiz quiz = a.getQuiz();
            StudentQuizDTO dto = new StudentQuizDTO();
            dto.setQuizId(quiz.getQuizId());
            dto.setTitle(quiz.getTitle());
            dto.setTopic(quiz.getTopic());
            dto.setDifficulty(quiz.getDifficulty());
            dto.setTotalMarks(quiz.getTotalMarks());
            dto.setStatus(a.getStatus());
            dto.setScore(a.getScore());
            result.add(dto);
        }
        return result;
    }
//
    // ================= QUIZ QUESTIONS =================

    @Override
    public List<QuizQuestionDTO> getQuestions(Long quizId) {

        List<QuizQuestion> questions = quizQuestionRepository.findByQuiz_QuizId(quizId);
        List<QuizQuestionDTO> result = new ArrayList<>();

        for (QuizQuestion q : questions) {

            QuizQuestionDTO dto = new QuizQuestionDTO();
            dto.setQuestionId(q.getQuestionId());
            dto.setQuestionText(q.getQuestion());
            dto.setQuestionType(q.getType());
            dto.setCorrectAnswer(q.getCorrectAnswer());

            // Only send options if they exist (LONG questions won't have them)
            List<String> options = new ArrayList<>();
            if (q.getOptionA() != null) options.add(q.getOptionA());
            if (q.getOptionB() != null) options.add(q.getOptionB());
            if (q.getOptionC() != null) options.add(q.getOptionC());
            if (q.getOptionD() != null) options.add(q.getOptionD());

            dto.setOptions(options);
            result.add(dto);
        }

        return result;
    }

//    @Transactional
//    public StudentQuizAttempt submitQuiz(Long quizId, String email, Map<Long, String> answers) {
//
//        // 1️⃣ Fetch quiz
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new RuntimeException("Quiz not found"));
//
//        // 2️⃣ Fetch student using JWT email
//        UserAuthentication user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Student student = studentRepository.findByUser(user)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        // 3️⃣ Check if attempt already exists
//        StudentQuizAttempt attempt = attemptRepository.findByQuizAndStudent(quiz, user)
//                .orElseGet(StudentQuizAttempt::new);
//
//        attempt.setQuiz(quiz);
//        attempt.setStudent(user);
//        attempt.setStatus(AttemptStatus.IN_PROGRESS);
//        attempt.setAutoScore(0);
//        attempt.setManualScore(0);
//        attempt.setAiScore(0);
//
//        attempt = attemptRepository.save(attempt);
//
//
//        List<StudentQuestionResponse> responses = new ArrayList<>();
//        boolean hasManual = false;
//        int autoScore = 0;
//
//        for (QuizQuestion question : quiz.getQuestions()) {
//
//            StudentQuestionResponse resp = new StudentQuestionResponse();
//            resp.setAttempt(attempt);
//            resp.setQuestion(question);
//            resp.setQuiz(quiz);
//
//            String submitted = answers.get(question.getQuestionId());
//            resp.setStudentAnswer(submitted);
//
//            // MCQ
//            if ("MCQ".equalsIgnoreCase(question.getType())) {
//
//                if (submitted != null && submitted.equalsIgnoreCase(question.getCorrectAnswer())) {
//                    resp.setMarksObtained(1);
//                    autoScore += 1;
//                } else {
//                    resp.setMarksObtained(0);
//                }
//
//                resp.setStatus(QuestionEvaluationStatus.AUTO_EVALUATED);
//                resp.setEvaluationStatus("AUTO_EVALUATED");
//            }
//            // LONG / SHORT
//            else {
//                resp.setMarksObtained(0);
//                resp.setStatus(QuestionEvaluationStatus.PENDING_MANUAL);
//                resp.setEvaluationStatus("PENDING_MANUAL");
//                hasManual = true;
//            }
//
//            studentQuestionResponseRepository.save(resp);
//        }
//
//// Finalize attempt
//        attempt.setAutoScore(autoScore);
//        attempt.setManualScore(0);
//        attempt.setAiScore(0);
//        attempt.setScore(autoScore);
//        attempt.setSubmittedAt(LocalDateTime.now());
//
//        if (hasManual) {
//            attempt.setStatus(AttemptStatus.PENDING_MANUAL_EVALUATION);
//        } else {
//            attempt.setStatus(AttemptStatus.COMPLETED);
//        }
//        // ================= UPDATE STUDENT QUIZ ASSIGNMENT =================
//        StudentQuizAssignment assignment =
//                assignmentRepository
//                        .findByStudent_IdAndQuiz_QuizId(student.getId(), quizId)
//                        .orElseThrow(() -> new RuntimeException("Assignment not found"));
//
//        assignment.setScore(autoScore);
//
//// If any long answers exist → it is completed but waiting for grading
//        if (hasManual) {
//            assignment.setStatus("PENDING_EVALUATION");   // Instructor should see this
//        } else {
//            assignment.setStatus("COMPLETED");            // Fully auto-graded
//        }
//
//        assignmentRepository.save(assignment);
//
//
//        return attemptRepository.save(attempt);
//    }
//
@Transactional
public StudentQuizAttempt submitQuiz(Long quizId, String email, Map<Long, String> answers) {

    // 1️⃣ Fetch quiz
    Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));

    // 2️⃣ Fetch student using JWT email
    UserAuthentication user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    Student student = studentRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    // 3️⃣ Fetch assignment (🔥 REQUIRED FOR ANALYTICS)
    StudentQuizAssignment assignment =
            assignmentRepository
                    .findByStudent_IdAndQuiz_QuizId(student.getId(), quizId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

    // 4️⃣ Check if attempt already exists
    StudentQuizAttempt attempt =
            attemptRepository.findByAssignment(assignment)
                    .orElseGet(StudentQuizAttempt::new);

    // 🔥 CRITICAL LINKS
    attempt.setAssignment(assignment);   // 🔥 THIS FIXES ANALYTICS
    attempt.setQuiz(quiz);
    attempt.setStudent(user);

    attempt.setStatus(AttemptStatus.IN_PROGRESS);
    attempt.setAutoScore(0);
    attempt.setManualScore(0);
    attempt.setAiScore(0);

    attempt = attemptRepository.save(attempt);

    boolean hasManual = false;
    int autoScore = 0;

    for (QuizQuestion question : quiz.getQuestions()) {

        StudentQuestionResponse resp = new StudentQuestionResponse();
        resp.setAttempt(attempt);
        resp.setQuestion(question);
        resp.setQuiz(quiz);

        String submitted = answers.get(question.getQuestionId());
        // For MCQ: each question is worth 1 mark
        int marksPerQuestion = 1;

        // MCQ
        if ("MCQ".equalsIgnoreCase(question.getType())) {

            if (submitted != null) {
                char[] letters = {'A', 'B', 'C', 'D'};
                int index = Integer.parseInt(submitted);
                String studentAnswerLetter = String.valueOf(letters[index]);

                resp.setStudentAnswer(studentAnswerLetter);

                if (studentAnswerLetter.equalsIgnoreCase(question.getCorrectAnswer())) {
                    resp.setMarksObtained(marksPerQuestion);
                    resp.setMarksAwarded(marksPerQuestion);
                    autoScore += marksPerQuestion;
                } else {
                    resp.setMarksObtained(0);
                    resp.setMarksAwarded(0);
                }
            } else {
                resp.setMarksObtained(0);
                resp.setMarksAwarded(0);
            }

            resp.setStatus(QuestionEvaluationStatus.AUTO_EVALUATED);
            resp.setEvaluationStatus("AUTO_EVALUATED");
        }
        // LONG / SHORT
        else {
            resp.setStudentAnswer(submitted);
            resp.setMarksObtained(0);
            resp.setStatus(QuestionEvaluationStatus.PENDING_MANUAL);
            resp.setEvaluationStatus("PENDING_MANUAL");
            hasManual = true;
        }

        studentQuestionResponseRepository.save(resp);
    }

    // 5️⃣ Finalize attempt
    attempt.setAutoScore(autoScore);
    attempt.setManualScore(0);
    attempt.setAiScore(0);
    attempt.setScore(autoScore);
    attempt.setSubmittedAt(LocalDateTime.now());

    if (hasManual) {
        attempt.setStatus(AttemptStatus.PENDING_MANUAL_EVALUATION);
        assignment.setStatus("PENDING_EVALUATION");
    } else {
        attempt.setStatus(AttemptStatus.COMPLETED);
        assignment.setStatus("COMPLETED");
    }

    assignment.setScore(autoScore);
    assignmentRepository.save(assignment);

    return attemptRepository.save(attempt);
}

    @Transactional
    public Quiz createManualQuiz(Quiz quiz, List<QuizQuestionDTO> questionDTOs, List<Long> studentIds) {

        quizRepository.save(quiz);   // save quiz first

        List<QuizQuestion> questions = new ArrayList<>();

        for (QuizQuestionDTO dto : questionDTOs) {
            QuizQuestion q = mapDtoToEntity(dto, quiz);
            questions.add(q);
        }

        quizQuestionRepository.saveAll(questions);

        quiz.setQuestions(questions);   // important for student fetch
        quizRepository.save(quiz);

        // assignment logic untouched

        return quiz;
    }

    private QuizQuestion mapDtoToEntity(QuizQuestionDTO dto, Quiz quiz) {

        QuizQuestion q = new QuizQuestion();
        q.setQuiz(quiz);

        q.setQuestion(dto.getQuestionText());
        q.setType(dto.getQuestionType());

        // MCQ mapping
        if ("MCQ".equalsIgnoreCase(dto.getQuestionType()) && dto.getOptions() != null) {
            List<String> ops = dto.getOptions();

            if (ops.size() > 0) q.setOptionA(ops.get(0));
            if (ops.size() > 1) q.setOptionB(ops.get(1));
            if (ops.size() > 2) q.setOptionC(ops.get(2));
            if (ops.size() > 3) q.setOptionD(ops.get(3));
        }

        q.setCorrectAnswer(dto.getCorrectAnswer());

        return q;
    }
    @Override
    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getTotalMarks() == null) {
            quiz.setTotalMarks(10);
        }
        if (quiz.getDifficulty() == null) {
            quiz.setDifficulty("EASY");
        }
        return quizRepository.save(quiz);
    }


}
