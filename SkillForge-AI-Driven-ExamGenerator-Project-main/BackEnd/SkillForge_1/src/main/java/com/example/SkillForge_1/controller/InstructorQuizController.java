////package com.example.SkillForge_1.controller;
////
////import com.example.SkillForge_1.dto.QuizCreateDTO;
////import com.example.SkillForge_1.model.Quiz;
////import com.example.SkillForge_1.model.Student;
////import com.example.SkillForge_1.service.InstructorQuizService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@RestController
////@RequestMapping("/api/instructor")
////public class InstructorQuizController {
////
////    @Autowired
////    private InstructorQuizService quizService;
////
////    @GetMapping("/students")
////    public ResponseEntity<List<Student>> getRegisteredStudents() {
////        return ResponseEntity.ok(quizService.getAllStudents());
////    }
////
////    @PostMapping("/quiz/create")
////    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDTO dto) {
////        Quiz created = quizService.createQuiz(dto);
////        return ResponseEntity.ok(created);
////    }
////}
//package com.example.SkillForge_1.controller;
//
//import com.example.SkillForge_1.dto.QuizCreateDTO;
//import com.example.SkillForge_1.dto.StudentDTO;
//import com.example.SkillForge_1.model.Quiz;
//import com.example.SkillForge_1.service.InstructorQuizService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/instructor")
//public class InstructorQuizController {
//
//    @Autowired
//    private InstructorQuizService quizService;
//
//    // Fetch students for assign dropdown
//    @GetMapping("/students")
//    public ResponseEntity<List<StudentDTO>> getRegisteredStudents() {
//        return ResponseEntity.ok(quizService.getAllStudents());
//    }
//
//
//    // Create quiz
//    @PostMapping("/quiz/create")
//    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDTO dto) {
//        Quiz created = quizService.createQuiz(dto);
//        return ResponseEntity.ok(created);
//    }
//    @PostMapping("/quiz/ai-create")
//    public ResponseEntity<?> createQuizFromAI(@RequestBody QuizCreateDTO dto) {
//
//        // This REUSES your existing quiz creation logic
//        Quiz created = quizService.createQuiz(dto);
//
//        return ResponseEntity.ok(created);
//    }
//
//}
package com.example.SkillForge_1.controller;

import com.example.SkillForge_1.dto.QuizCreateDTO;
import com.example.SkillForge_1.dto.QuizListDTO;
import com.example.SkillForge_1.dto.QuizQuestionDTO;
import com.example.SkillForge_1.dto.StudentDTO;
import com.example.SkillForge_1.model.Quiz;
import com.example.SkillForge_1.repository.StudentQuizAssignmentRepository;
import com.example.SkillForge_1.service.InstructorQuizService;
import com.example.SkillForge_1.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/instructor")
public class InstructorQuizController {

    @Autowired
    private InstructorQuizService quizService;

    @Autowired
    private StudentQuizAssignmentRepository assignmentRepository;

    @Autowired
    private QuizRepository quizRepository; // ✅ inject repository for /quiz/all

    // Fetch students for assign dropdown
    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getRegisteredStudents() {
        return ResponseEntity.ok(quizService.getAllStudents());
    }

    // Create quiz
    @PostMapping("/quiz/create")
    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDTO dto) {
        Quiz created = quizService.createQuiz(dto);
        return ResponseEntity.ok(created);
    }
//    @PostMapping("/quiz/ai-create")
//    public ResponseEntity<?> createQuizFromAI(@RequestBody QuizCreateDTO dto) {
//        System.out.println("AI QUIZ DTO = " + dto);
//        Quiz created = quizService.createQuiz(dto);
//        return ResponseEntity.ok(created);
//    }
@PostMapping("/quiz/ai-create")
public ResponseEntity<?> createQuizFromAI(@RequestBody QuizCreateDTO dto) {
    System.out.println("AI QUIZ DTO = " + dto);
    Quiz created = quizService.createQuiz(dto);
    return ResponseEntity.ok(created);
}


    // ✅ NEW: Get all quizzes
    @GetMapping("/quiz/all")
    public ResponseEntity<List<QuizListDTO>> getAllQuizzes() {

        List<QuizListDTO> quizzes = quizRepository.findAll()
                .stream()
                .map(q -> new QuizListDTO(
                        q.getQuizId(),
                        q.getTitle(),
                        q.getDifficulty(),
                        q.getTotalMarks(),
                        q.getTopicId(),
                        q.isActive()
                ))
                .toList();

        return ResponseEntity.ok(quizzes);
    }
    @PutMapping("/quiz/{id}")
    public ResponseEntity<Void> updateQuiz(
            @PathVariable Long id,
            @RequestBody QuizCreateDTO dto) {

        quizService.updateQuiz(id, dto);
        return ResponseEntity.ok().build(); // ✅ NO ENTITY RETURNED
    }
    @DeleteMapping("/quiz/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
