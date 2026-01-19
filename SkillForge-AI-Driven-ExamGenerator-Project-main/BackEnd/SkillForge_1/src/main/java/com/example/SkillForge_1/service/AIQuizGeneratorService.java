//package com.example.SkillForge_1.service;
//import com.example.SkillForge_1.model.Quiz;
//import com.example.SkillForge_1.model.QuizQuestion;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//
//import java.util.*;
//
//@Service
//public class AIQuizGeneratorService {
//
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//
//    @Value("${gemini.model}")
//    private String geminiModel;
//
//    public List<QuizQuestion> generateQuestions(Quiz quiz) {
//        List<QuizQuestion> questions = new ArrayList<>();
//
//        // Prompt to Gemini
//        String prompt = "Generate 5 EASY multiple-choice questions for the topic: "
//                + quiz.getTitle()
//                + ". Return ONLY valid JSON array like: "
//                + "[{ \"questionText\": \"...\", \"options\": [\"A\",\"B\",\"C\",\"D\"], \"correctAnswer\": \"A\" }]";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + geminiApiKey);
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("model", geminiModel);
//        body.put("prompt", prompt);
//        body.put("temperature", 0.7);
//        body.put("max_output_tokens", 500);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.postForEntity(
//                    "https://generativelanguage.googleapis.com/v1beta/models/" + geminiModel + ":generateContent",
//                    request,
//                    String.class
//            );
//
//            String json = response.getBody();
//            ObjectMapper mapper = new ObjectMapper();
//            List<Map<String, Object>> aiQuestions = mapper.readValue(json, List.class);
//
//            for (Map<String, Object> aiQ : aiQuestions) {
//                QuizQuestion q = new QuizQuestion();
//                q.setQuestion((String) aiQ.get("questionText"));
//                q.setType("MCQ"); // always MCQ for now
//
//                List<String> opts = (List<String>) aiQ.get("options");
//                q.setOptionA(opts.get(0));
//                q.setOptionB(opts.get(1));
//                q.setOptionC(opts.get(2));
//                q.setOptionD(opts.get(3));
//
//                q.setCorrectAnswer((String) aiQ.get("correctAnswer"));
//                q.setQuiz(quiz);
//
//                questions.add(q);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return questions;
//    }
//}

//
//    public List<QuizQuestion> generateQuestions(Quiz quiz) {
//
//        List<QuizQuestion> questions = new ArrayList<>();
//
//        // ✅ Strong prompt to force clean JSON
//        String prompt = """
//        Generate 5 EASY multiple-choice questions on the topic: %s.
//
//        Rules:
//        - Return ONLY valid JSON
//        - No markdown
//        - No explanation
//        - Exactly 4 options per question
//
//        Format:
//        [
//          {
//            "questionText": "Question text",
//            "options": ["A", "B", "C", "D"],
//            "correctAnswer": "A"
//          }
//        ]
//        """.formatted(quiz.getTitle());
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // ✅ Correct Gemini headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 🔥 Gemini uses API key as query param, NOT Authorization header
//        String url =
//                "https://generativelanguage.googleapis.com/v1beta/models/"
//                        + geminiModel
//                        + ":generateContent?key="
//                        + geminiApiKey;
//
//        // ✅ Correct Gemini request body
//        Map<String, Object> body = new HashMap<>();
//        body.put("contents", List.of(
//                Map.of(
//                        "parts", List.of(
//                                Map.of("text", prompt)
//                        )
//                )
//        ));
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<String> response =
//                    restTemplate.postForEntity(url, request, String.class);
//
//            String rawResponse = response.getBody();
//
//            System.out.println("========== RAW GEMINI RESPONSE ==========");
//            System.out.println(rawResponse);
//            System.out.println("========================================");
//
//            ObjectMapper mapper = new ObjectMapper();
//
//            // 🔍 Parse Gemini wrapper
//            Map<String, Object> root =
//                    mapper.readValue(rawResponse, Map.class);
//
//            List<Map<String, Object>> candidates =
//                    (List<Map<String, Object>>) root.get("candidates");
//
//            if (candidates == null || candidates.isEmpty()) {
//                System.out.println("❌ No candidates returned by Gemini");
//                return questions;
//            }
//
//            Map<String, Object> content =
//                    (Map<String, Object>) candidates.get(0).get("content");
//
//            List<Map<String, Object>> parts =
//                    (List<Map<String, Object>>) content.get("parts");
//
//            String aiText = (String) parts.get(0).get("text");
//
//            System.out.println("========== EXTRACTED AI JSON ==========");
//            System.out.println(aiText);
//            System.out.println("======================================");
//
//            // ✅ Parse actual JSON array
//            List<Map<String, Object>> aiQuestions =
//                    mapper.readValue(aiText, List.class);
//
//            for (Map<String, Object> aiQ : aiQuestions) {
//
//                QuizQuestion q = new QuizQuestion();
//                q.setQuestion((String) aiQ.get("questionText"));
//                q.setType("MCQ");
//
//                List<String> options =
//                        (List<String>) aiQ.get("options");
//
//                q.setOptionA(options.get(0));
//                q.setOptionB(options.get(1));
//                q.setOptionC(options.get(2));
//                q.setOptionD(options.get(3));
//
//                q.setCorrectAnswer(
//                        (String) aiQ.get("correctAnswer")
//                );
//
//                q.setQuiz(quiz);
//                questions.add(q);
//            }
//
//            System.out.println("✅ AI GENERATED QUESTIONS COUNT: " + questions.size());
//
//        } catch (Exception e) {
//            System.out.println("❌ AI GENERATION FAILED");
//            e.printStackTrace();
//        }
//
//        return questions;
//    }
//}
package com.example.SkillForge_1.service;

import com.example.SkillForge_1.model.Quiz;
import com.example.SkillForge_1.model.QuizQuestion;
import com.example.SkillForge_1.repository.QuizQuestionRepository;
import com.example.SkillForge_1.repository.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIQuizGeneratorService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.model}")
    private String geminiModel;

    @Autowired
    private QuizRepository quizRepository; // <-- Add this

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    /**
     * Generate AI questions for a quiz and save them to the database.
     * Supports MCQ, LONG, and SHORT question types.
     */
//    public List<QuizQuestion> generateQuestions(Quiz quiz) {
//
//        List<QuizQuestion> questions = new ArrayList<>();
//        String type = quiz.getQuestionType(); // MCQ, LONG, SHORT
//        String prompt;
//
//        // ---------------- PROMPT GENERATION ----------------
//        if ("MCQ".equalsIgnoreCase(type)) {
//            prompt = """
//                    Generate 5 EASY multiple-choice questions on the topic: %s.
//
//                    Rules:
//                    - Return ONLY valid JSON
//                    - No markdown
//                    - No explanation
//                    - Exactly 4 options per question
//
//                    Format:
//                    [
//                      {
//                        "questionText": "Question text",
//                        "options": ["A", "B", "C", "D"],
//                        "correctAnswer": "A"
//                      }
//                    ]
//                    """.formatted(quiz.getTitle());
//        } else if ("LONG".equalsIgnoreCase(type)) {
//            prompt = """
//                    Generate 5 LONG ANSWER questions on the topic: %s.
//
//                    Rules:
//                    - Return ONLY valid JSON
//                    - No options
//                    - No correctAnswer
//                    - Each question must require paragraph-length answers
//
//                    Format:
//                    [
//                      {
//                        "questionText": "Explain ..."
//                      }
//                    ]
//                    """.formatted(quiz.getTitle());
//        } else { // SHORT
//            prompt = """
//                    Generate 5 SHORT ANSWER questions on the topic: %s.
//
//                    Rules:
//                    - Return ONLY valid JSON
//                    - No options
//                    - No correctAnswer
//                    - Each answer should be 1–2 lines
//
//                    Format:
//                    [
//                      {
//                        "questionText": "Define ..."
//                      }
//                    ]
//                    """.formatted(quiz.getTitle());
//        }
//
//        // ---------------- API CALL ----------------
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String url =
//                "https://generativelanguage.googleapis.com/v1beta/models/"
//                        + geminiModel
//                        + ":generateContent?key="
//                        + geminiApiKey;
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("contents", List.of(
//                Map.of("role", "user",
//                        "parts", List.of(Map.of("text", prompt)))
//        ));
//        body.put("generationConfig", Map.of(
//                "temperature", 0.2,
//                "response_mime_type", "application/json"
//        ));
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//            String raw = response.getBody();
//
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> root = mapper.readValue(raw, Map.class);
//
//            List<Map<String, Object>> candidates = (List<Map<String, Object>>) root.get("candidates");
//            if (candidates == null || candidates.isEmpty()) return questions;
//
//            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
//            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
//            String aiText = (String) parts.get(0).get("text");
//
//            // ---------------- PARSE AI QUESTIONS ----------------
//            List<Map<String, Object>> aiQuestions = mapper.readValue(aiText, List.class);
//
//            for (Map<String, Object> aiQ : aiQuestions) {
//                QuizQuestion q = new QuizQuestion();
//                q.setQuestion((String) aiQ.get("questionText"));
//                q.setType(type);
//                q.setQuiz(quiz);
//
//                if ("MCQ".equalsIgnoreCase(type)) {
//                    List<String> options = (List<String>) aiQ.get("options");
//                    q.setOptionA(options.get(0));
//                    q.setOptionB(options.get(1));
//                    q.setOptionC(options.get(2));
//                    q.setOptionD(options.get(3));
//                    q.setCorrectAnswer((String) aiQ.get("correctAnswer"));
//                }
//
//                // ---------------- SAVE TO DB ----------------
//                q.setType(type);
//                quizQuestionRepository.save(q);
//                questions.add(q);
//            }
//
//            System.out.println("✅ Generated and saved " + questions.size() + " " + type + " questions");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return questions;
//    }
    @Transactional
    public List<QuizQuestion> generateQuestions(Quiz quiz) {

        List<QuizQuestion> questions = new ArrayList<>();
        String type = quiz.getQuestionType(); // MCQ, LONG, SHORT
        String prompt;

        // ---------------- SAVE QUIZ FIRST ----------------
       // Important! Save parent first

        // ---------------- PROMPT GENERATION ----------------
        if ("MCQ".equalsIgnoreCase(type)) {
            prompt = """
                Generate 5 EASY multiple-choice questions on the topic: %s.
                
                Rules:
                - Return ONLY valid JSON
                - No markdown
                - No explanation
                - Exactly 4 options per question
                
                Format:
                [
                  {
                    "questionText": "Question text",
                    "options": ["A", "B", "C", "D"],
                    "correctAnswer": "A"
                  }
                ]
                """.formatted(quiz.getTitle());
        } else if ("LONG".equalsIgnoreCase(type)) {
            prompt = """
                Generate 5 LONG ANSWER questions on the topic: %s.
                
                Rules:
                - Return ONLY valid JSON
                - No options
                - No correctAnswer
                - Each question must require paragraph-length answers
                
                Format:
                [
                  {
                    "questionText": "Explain ..."
                  }
                ]
                """.formatted(quiz.getTitle());
        } else { // SHORT
            prompt = """
                Generate 5 SHORT ANSWER questions on the topic: %s.
                
                Rules:
                - Return ONLY valid JSON
                - No options
                - No correctAnswer
                - Each answer should be 1–2 lines
                
                Format:
                [
                  {
                    "questionText": "Define ..."
                  }
                ]
                """.formatted(quiz.getTitle());
        }

        // ---------------- API CALL ----------------
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/"
                        + geminiModel
                        + ":generateContent?key="
                        + geminiApiKey;

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(
                Map.of("role", "user",
                        "parts", List.of(Map.of("text", prompt)))
        ));
        body.put("generationConfig", Map.of(
                "temperature", 0.2,
                "response_mime_type", "application/json"
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String raw = response.getBody();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> root = mapper.readValue(raw, Map.class);

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) root.get("candidates");
            if (candidates == null || candidates.isEmpty()) return questions;

            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            String aiText = (String) parts.get(0).get("text");

            // ---------------- PARSE AI QUESTIONS ----------------
            List<Map<String, Object>> aiQuestions = mapper.readValue(aiText, List.class);

            for (Map<String, Object> aiQ : aiQuestions) {
                QuizQuestion q = new QuizQuestion();
                q.setQuestion((String) aiQ.get("questionText"));
                q.setType(type);
                q.setQuiz(quiz); // quiz is already saved

                if ("MCQ".equalsIgnoreCase(type)) {
                    List<String> options = (List<String>) aiQ.get("options");
                    q.setOptionA(options.get(0));
                    q.setOptionB(options.get(1));
                    q.setOptionC(options.get(2));
                    q.setOptionD(options.get(3));
                    q.setCorrectAnswer((String) aiQ.get("correctAnswer"));
                }

                questions.add(q);

                // ---------------- SAVE TO DB ----------------
//                quizQuestionRepository.save(q);
//                questions.add(q);
            }

            System.out.println("✅ Generated and saved " + questions.size() + " " + type + " questions");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

}
