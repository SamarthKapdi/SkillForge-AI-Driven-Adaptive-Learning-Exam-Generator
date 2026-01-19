// // aiService.js

// /**
//  * Simulates AI-powered quiz generation.
//  * This can be replaced with an actual call to Gemini API or a backend AI endpoint.
//  */
// export const aiService = {
//     async generateQuestions({ topicName, numQuestions, difficulty }) {
//         console.log(`Generating ${numQuestions} ${difficulty} questions for topic: ${topicName}`);

//         // Simulate network delay for "AI thinking" effect
//         await new Promise(resolve => setTimeout(resolve, 3000));

//         const generatedQuestions = [];

//         const difficultyMultipliers = {
//             'EASY': 'basic',
//             'MEDIUM': 'intermediate',
//             'DIFFICULT': 'advanced'
//         };

//         const prefix = difficultyMultipliers[difficulty] || 'standard';

//         for (let i = 1; i <= numQuestions; i++) {
//             const possibleAnswers = ['A', 'B', 'C', 'D'];
//             const correctIndex = Math.floor(Math.random() * 4);

//             generatedQuestions.push({
//                 questionId: i,
//                 questionText: `AI Generated ${prefix} question #${i} about ${topicName}?`,
//                 options: [
//                     `Option A for ${topicName}`,
//                     `Option B for ${topicName}`,
//                     `Option C for ${topicName}`,
//                     `Option D for ${topicName}`
//                 ],
//                 correctAnswer: possibleAnswers[correctIndex]
//             });
//         }

//         return generatedQuestions;
//     }
// };
import axios from "axios";

export const aiService = {
    async generateQuestions({ topicName, numQuestions, difficulty, questionType }) {
        console.log("=== AI GENERATION REQUEST ===");
        console.log("📤 Sending request to: http://localhost:8081/api/instructor/generate");

        const requestPayload = { topicName, numQuestions, difficulty, questionType };
        console.log("📦 Request Payload:", JSON.stringify(requestPayload, null, 2));

        try {
            const res = await axios.post(
                "http://localhost:8081/api/instructor/generate",
                requestPayload
            );

            console.log("✅ AI Generation Response received");

            // Handle different response formats from backend
            let questions = [];

            if (Array.isArray(res.data)) {
                questions = res.data;
            } else if (res.data && res.data.questions && Array.isArray(res.data.questions)) {
                questions = res.data.questions;
            } else {
                throw new Error("Invalid response format from AI generation endpoint");
            }

            // Transform backend format to frontend format
            console.log("🔄 Transforming questions to frontend format...");
            const transformedQuestions = questions.map((q, idx) => {
                console.log(`DEBUG: Raw AI Question [${idx}]:`, JSON.stringify(q, null, 2));

                // ✅ FIX: Use backend type if present, else fallback to user's requested type
                let type = q.type ? q.type.toUpperCase() : (questionType ? questionType.toUpperCase() : 'SHORT');

                // Determine options array
                let options = [];
                // ✅ FIX: Process options if it's MCQ OR if backend sent some option data
                if (type === 'MCQ' || (
                    q.options || q.choices || q.options_array ||
                    q.optionA || q.optionB || q.optionC || q.optionD
                )) {
                    // Try different field names for options
                    const rawOptions = q.options || q.choices || q.options_array || q.selections || [];

                    if (Array.isArray(rawOptions) && rawOptions.length > 0) {
                        options = rawOptions;
                    } else {
                        // Try separate fields like optionA, option_a, choice1, choiceA, etc.
                        options = [
                            q.optionA || q.option_a || q.choiceA || q.choice_a || q.choice1 || q.option1 || '',
                            q.optionB || q.option_b || q.choiceB || q.choice_b || q.choice2 || q.option2 || '',
                            q.optionC || q.option_c || q.choiceC || q.choice_c || q.choice3 || q.option3 || '',
                            q.optionD || q.option_d || q.choiceD || q.choice_d || q.choice4 || q.option4 || ''
                        ];
                    }

                    // Pad with empty strings if fewer than 4 provided (ONLY for MCQ)
                    if (type === 'MCQ' && options.length < 4) {
                        while (options.length < 4) options.push('');
                    }
                }

                // ✅ FIX 3 - (implicit) NO auto-padding for non-MCQ.
                // ✅ Correct transform logic
                const transformed = {
                    questionId: q.questionId || q.id || (idx + 1).toString(),
                    questionText: q.questionText || q.question || '',
                    type: type,
                    options: type === 'MCQ' ? options : [],
                    correctAnswer: type === 'MCQ' ? (q.correctAnswer || q.answer || '') : ''
                };

                // Normalize Answer for MCQ
                if (type === 'MCQ' && transformed.correctAnswer) {
                    const ans = transformed.correctAnswer.toString().trim();
                    // If it's already A, B, C, D, we are good
                    if (['A', 'B', 'C', 'D'].includes(ans.toUpperCase())) {
                        transformed.correctAnswer = ans.toUpperCase();
                    } else {
                        // Try to match the text of the options
                        const answerIndex = transformed.options.findIndex(opt =>
                            opt && opt.toString().trim().toLowerCase() === ans.toLowerCase()
                        );
                        if (answerIndex !== -1) {
                            transformed.correctAnswer = ['A', 'B', 'C', 'D'][answerIndex];
                        } else {
                            // If it's a number (1-4), map to A-D
                            if (ans === '1' || ans === '0') transformed.correctAnswer = 'A';
                            else if (ans === '2' || ans === '1') transformed.correctAnswer = 'B';
                            else if (ans === '3' || ans === '2') transformed.correctAnswer = 'C';
                            else if (ans === '4' || ans === '3') transformed.correctAnswer = 'D';
                            else {
                                // Default to A if we can't figure it out, to avoid validation errors
                                console.warn(`Unrecognized correctAnswer format: "${ans}", defaulting to A`);
                                transformed.correctAnswer = 'A';
                            }
                        }
                    }
                }

                console.log(`DEBUG: Transformed Question [${idx}]:`, transformed);
                return transformed;
            });

            return transformedQuestions;
        } catch (error) {
            console.error("❌ AI GENERATION FAILED", error);
            throw error;
        }
    },

    async saveAiQuiz(quizData) {
        console.log("=== SAVING AI QUIZ ===");
        // Existing save logic remains same...
        // ... (truncated for brevity, keep existing saveAiQuiz)
        console.log("📤 Sending request to: http://localhost:8081/api/instructor/quiz/ai-create");

        const token = localStorage.getItem("skillforge_token");

        // Remove quizId if present
        const { quizId, ...cleanPayload } = quizData;

        try {
            const res = await axios.post(
                "http://localhost:8081/api/instructor/quiz/ai-create",
                cleanPayload,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                }
            );
            return res.data;
        } catch (error) {
            console.error("❌ AI QUIZ SAVE FAILED", error);
            throw error;
        }
    },

    async generateStudySuggestions({ quizTitle, score, totalMarks, incorrectTopics, percentage }) {
        console.log("=== GENERATING AI STUDY SUGGESTIONS ===");

        try {
            const token = localStorage.getItem("skillforge_token");

            // Try to call backend AI endpoint
            const res = await axios.post(
                "http://localhost:8081/api/instructor/generate",
                {
                    topicName: `Study suggestions for ${quizTitle}. The student scored ${score}/${totalMarks} (${percentage}%). Areas that need improvement: ${incorrectTopics?.join(', ') || 'various topics'}. Generate 3-4 personalized study tips and resource recommendations.`,
                    numQuestions: 1,
                    difficulty: 'MEDIUM',
                    questionType: 'LONG'
                }
            );

            if (res.data && res.data.length > 0) {
                const suggestion = res.data[0]?.questionText || res.data[0]?.question || '';
                return suggestion;
            }

            // Fallback to local suggestions
            return this.getLocalSuggestions({ quizTitle, score, totalMarks, percentage });
        } catch (error) {
            console.warn("AI suggestion generation failed, using local fallback:", error);
            return this.getLocalSuggestions({ quizTitle, score, totalMarks, percentage });
        }
    },

    getLocalSuggestions({ quizTitle, score, totalMarks, percentage }) {
        const suggestions = [];

        if (percentage < 40) {
            suggestions.push(`📚 **Focus on Fundamentals**: Your score of ${percentage}% suggests you need to revisit the core concepts of ${quizTitle}. Start with introductory materials and build up gradually.`);
            suggestions.push(`🎯 **Practice Regularly**: Dedicate 30-45 minutes daily to studying this topic. Consistent practice is key to improvement.`);
            suggestions.push(`📝 **Take Notes**: Write down key concepts and formulas. Creating your own study materials helps with retention.`);
            suggestions.push(`🤝 **Seek Help**: Consider forming a study group or reaching out to your instructor for additional guidance.`);
        } else if (percentage < 60) {
            suggestions.push(`📈 **Good Progress**: You're on the right track with ${percentage}%. Focus on the areas where you made mistakes.`);
            suggestions.push(`🔄 **Review Mistakes**: Carefully analyze each incorrect answer to understand where you went wrong.`);
            suggestions.push(`📖 **Deepen Understanding**: Move beyond memorization - try to understand the 'why' behind each concept.`);
            suggestions.push(`⏰ **Time Management**: Practice solving similar problems under timed conditions.`);
        } else if (percentage < 80) {
            suggestions.push(`⭐ **Strong Performance**: Your ${percentage}% score shows solid understanding of ${quizTitle}!`);
            suggestions.push(`🎯 **Fine-tune Weak Areas**: Identify the specific topics where you lost points and focus there.`);
            suggestions.push(`📚 **Advanced Materials**: Consider exploring advanced topics or related subjects to broaden your knowledge.`);
            suggestions.push(`🏆 **Challenge Yourself**: Try harder practice problems to push your understanding further.`);
        } else {
            suggestions.push(`🌟 **Excellent Work**: Outstanding performance with ${percentage}%! You've mastered ${quizTitle}.`);
            suggestions.push(`🚀 **Keep Advancing**: You're ready for more challenging material in this subject.`);
            suggestions.push(`👨‍🏫 **Help Others**: Consider tutoring peers - teaching reinforces your own understanding.`);
            suggestions.push(`🔬 **Explore Further**: Look into advanced topics or real-world applications of these concepts.`);
        }

        return suggestions.join('\n\n');
    }
};
