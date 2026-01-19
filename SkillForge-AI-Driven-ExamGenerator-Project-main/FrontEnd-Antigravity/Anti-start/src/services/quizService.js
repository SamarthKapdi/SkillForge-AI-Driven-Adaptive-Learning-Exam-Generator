import axios from "axios";

const API_BASE_URL = "http://localhost:8081/student";

const getAuthHeader = () => {
    const token = localStorage.getItem("skillforge_token");
    return {
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    };
};

export const quizService = {
    // ---------------- INSTRUCTOR/CORE QUIZ ENDPOINTS ----------------
    async getAllQuizzes() {
        console.log("🔍 ========== FETCHING ALL QUIZZES ==========");
        const endpoint = "http://localhost:8081/api/instructor/quiz/all";
        console.log("📤 Endpoint:", endpoint);

        const token = localStorage.getItem("skillforge_token");
        console.log("🔑 Token:", token ? `${token.substring(0, 20)}...` : "NOT FOUND");

        try {
            const response = await axios.get(endpoint, getAuthHeader());
            console.log("✅ Response Status:", response.status);
            console.log("📥 Response Data:", response.data);
            console.log("📊 Number of quizzes:", response.data?.length || 0);

            if (response.data && Array.isArray(response.data)) {
                response.data.forEach((quiz, idx) => {
                    console.log(`  Quiz ${idx + 1}:`, {
                        quizId: quiz.quizId || quiz.id,
                        title: quiz.title,
                        difficulty: quiz.difficulty,
                        topicId: quiz.topicId,
                        questionsCount: quiz.questions?.length || 0,
                        attemptsCount: quiz.attempts?.length || 0,
                        assignedCount: quiz.assignedStudents?.length || 0
                    });
                });
            }

            console.log("✅ ========== FETCH QUIZZES SUCCESS ==========\n");
            return response.data || [];
        } catch (error) {
            console.error("❌ ========== FETCH QUIZZES FAILED ==========");
            console.error("Error Details:", {
                message: error.message,
                status: error.response?.status,
                statusText: error.response?.statusText,
                data: error.response?.data,
                endpoint: endpoint
            });
            console.error("Full Error:", error);
            console.log("❌ ========== FETCH ERROR END ==========\n");
            throw error;
        }
    },

    async getStudentLatestAttempt(quizId) {
        const res = await axios.get(
            `http://localhost:8081/api/student/quiz/${quizId}/latest-attempt`,
            getAuthHeader()
        );
        return res.data;   // { attemptId: 123 }
    },

    async getQuizById(quizId) {
        try {
            // Try instructor endpoint first to get full quiz details including timeLimit
            const instructorEndpoint = `http://localhost:8081/api/instructor/quiz/${quizId}`;
            console.log("🔍 Fetching quiz details from instructor endpoint:", instructorEndpoint);
            
            const response = await axios.get(instructorEndpoint, getAuthHeader());
            console.log("✅ Instructor endpoint response:", response.data);
            
            // If timeLimit is missing, try to get from localStorage
            if (response.data && !response.data.timeLimit) {
                const storedTimeLimit = localStorage.getItem(`quiz_timeLimit_${response.data.quizId}`);
                if (storedTimeLimit) {
                    response.data.timeLimit = parseInt(storedTimeLimit, 10);
                    console.log(`📚 Retrieved stored timeLimit ${storedTimeLimit} for quiz ${response.data.quizId}`);
                }
            }
            
            return response.data;
        } catch (instructorError) {
            console.warn("❌ Instructor endpoint failed, falling back to student endpoint:", instructorError.message);
            try {
                const response = await axios.get(`${API_BASE_URL}/quizzes`, getAuthHeader());
                // find the quiz by id
                const quiz = response.data.find(q => q.quizId === Number(quizId));
                
                // If timeLimit is missing, try to get from localStorage
                if (quiz && !quiz.timeLimit) {
                    const storedTimeLimit = localStorage.getItem(`quiz_timeLimit_${quiz.quizId}`);
                    if (storedTimeLimit) {
                        quiz.timeLimit = parseInt(storedTimeLimit, 10);
                        console.log(`📚 Retrieved stored timeLimit ${storedTimeLimit} for quiz ${quiz.quizId}`);
                    }
                }
                
                return quiz || null;
            } catch (studentError) {
                console.error("❌ Both endpoints failed. Student error:", studentError.message);
                throw studentError;
            }
        }
    },

    // ---------------- STUDENT-SPECIFIC QUIZ ENDPOINTS ----------------
    async getAssignedQuizzes() {
        try {
            const response = await axios.get(
                `${API_BASE_URL}/quizzes`,
                getAuthHeader()
            );
            return response.data || [];
        } catch (error) {
            console.error("Error fetching assigned quizzes:", error);
            throw error;
        }
    },
    async getStudentLatestAttempt(quizId) {
        try {
            const response = await axios.get(
                `http://localhost:8081/api/student/quiz/${quizId}/latest-attempt`,
                getAuthHeader()
            );
            return response.data;
        } catch (error) {
            console.error("Error fetching latest attempt:", error);
            throw error;
        }
    },
    async getQuestionsByQuizId(quizId) {
        try {
            // Get questions for specific quiz
            const response = await axios.get(`${API_BASE_URL}/quiz/${quizId}/questions`, getAuthHeader());
            return response.data || [];
        } catch (error) {
            console.warn(`Specific questions endpoint /quiz/${quizId}/questions failed, trying via quiz object`);
            try {
                const quiz = await this.getQuizById(quizId);
                return quiz.questions || [];
            } catch (err) {
                console.error("Could not fetch questions via any method", err);
                return [];
            }
        }
    },

    async submitQuizAttempt(quizId, answerMap, studentId) {
        console.log("🚀 ========== SUBMITTING QUIZ ATTEMPT ==========");
        try {
            const token = localStorage.getItem("skillforge_token");
            const url = studentId
                ? `${API_BASE_URL}/quiz/${quizId}/submit?studentId=${studentId}`
                : `${API_BASE_URL}/quiz/${quizId}/submit`;

            console.log("📤 POST URL:", url);
            console.log("🔑 Auth Token Present:", !!token);
            console.log("📦 Payload (Raw Map):", JSON.stringify(answerMap, null, 2));

            const response = await axios.post(
                url,
                answerMap,
                getAuthHeader()
            );

            console.log("✅ Submission Request Successful!");
            console.log("📥 Status:", response.status);
            console.log("📥 Data:", response.data);
            console.log("🚀 ========== SUBMISSION SUCCESS ==========\n");
            return response.data;
        } catch (error) {
            console.error("❌ ========== SUBMISSION FAILED ==========");
            console.error("Error Details:", {
                message: error.message,
                status: error.response?.status,
                data: error.response?.data,
                url: error.config?.url
            });
            console.log("❌ ========== SUBMISSION ERROR END ==========\n");
            throw error;
        }
    },

    async getStudentAttemptMeta(attemptId) {
        try {
            const response = await axios.get(`http://localhost:8081/api/instructor/analytics/review/${attemptId}`, getAuthHeader());
            return response.data;
        } catch (error) {
            console.error("Error fetching attempt meta:", error);
            throw error;
        }
    },

    async submitGrade(responseId, marks) {
        try {
            console.log(`📤 Submitting marks for response ${responseId}:`, marks);
            const response = await axios.put(
                `http://localhost:8081/api/instructor/evaluation/${responseId}`,
                { marks },
                getAuthHeader()
            );
            return response.data;
        } catch (error) {
            console.error("Error submitting grade:", error);
            throw error;
        }
    },

    // ---------------- INSTRUCTOR-SPECIFIC MANAGEMENT ----------------
    async getRegisteredStudents() {
        try {
            // Updated to /api/instructor/students as per implementation plan
            const response = await axios.get(`http://localhost:8081/api/instructor/students`, getAuthHeader());
            return response.data || [];
        } catch (error) {
            console.error("Error fetching students:", error);
            throw error;
        }
    },

    async createAndAssignQuiz(quizData) {
        console.log("=== MANUAL QUIZ CREATION: STARTED ===");
        const endpoint = "http://localhost:8081/api/instructor/quiz/create";
        console.log("📤 Endpoint:", endpoint);

        const token = localStorage.getItem("skillforge_token");
        console.log("🔑 Token:", token ? `${token.substring(0, 20)}...` : "NOT FOUND");

        console.log("📦 Payload:", JSON.stringify(quizData, null, 2));

        try {
            // Create quiz and assign to students in one go
            const response = await axios.post(endpoint, quizData, getAuthHeader());

            console.log("✅ Manual Quiz Creation Response received");
            console.log("📥 Response Status:", response.status);
            console.log("📥 Response Data:", JSON.stringify(response.data, null, 2));
            
            // Store timeLimit in localStorage for frontend timer functionality
            if (response.data && response.data.quizId && quizData.timeLimit) {
                localStorage.setItem(`quiz_timeLimit_${response.data.quizId}`, quizData.timeLimit.toString());
                console.log(`💾 Stored timeLimit ${quizData.timeLimit} for quiz ${response.data.quizId}`);
            }
            
            console.log("=== MANUAL QUIZ CREATION: SUCCESS ===\n");
            return response.data;
        } catch (error) {
            console.error("❌ MANUAL QUIZ CREATION FAILED");
            console.error("Error Details:", {
                message: error.message,
                status: error.response?.status,
                statusText: error.response?.statusText,
                data: error.response?.data
            });
            console.error("Full Error:", error);
            console.log("=== MANUAL QUIZ CREATION: ERROR ===\n");
            throw error;
        }
    },

    async updateQuiz(quizId, quizData) {
        console.log(`=== UPDATE QUIZ (${quizId}): STARTED ===`);
        const endpoint = `http://localhost:8081/api/instructor/quiz/${quizId}`;
        console.log("📤 Endpoint:", endpoint);

        const token = localStorage.getItem("skillforge_token");
        console.log("🔑 Token:", token ? `${token.substring(0, 20)}...` : "NOT FOUND");
        console.log("📦 Payload:", JSON.stringify(quizData, null, 2));

        try {
            const response = await axios.put(endpoint, quizData, getAuthHeader());

            console.log("✅ Update Quiz Response received");
            console.log("📥 Response Status:", response.status);
            console.log("📥 Response Data:", JSON.stringify(response.data, null, 2));
            console.log("=== UPDATE QUIZ: SUCCESS ===\n");
            return response.data;
        } catch (error) {
            console.error("❌ UPDATE QUIZ FAILED");
            console.error("Error Details:", {
                message: error.message,
                status: error.response?.status,
                statusText: error.response?.statusText,
                data: error.response?.data
            });
            console.error("Full Error:", error);
            console.log("=== UPDATE QUIZ: ERROR ===\n");
            throw error;
        }
    },

    async deleteQuiz(quizId) {
        try {
            const response = await axios.delete(`http://localhost:8081/api/instructor/quiz/${quizId}`, getAuthHeader());
            return response.data;
        } catch (error) {
            console.error("Error deleting quiz:", error);
            throw error;
        }
    },

    // ---------------- ANALYTICS & DETAILED VIEWS ----------------
    async getQuizAnalytics(quizId) {
        try {
            // Get quiz with analytics data (questions + student attempts)
            const response = await axios.get(`http://localhost:8081/api/instructor/quiz/${quizId}/analytics`, getAuthHeader());
            return response.data;
        } catch (error) {
            console.error("Error fetching quiz analytics:", error);
            throw error;
        }
    },

    async getQuizAttempts(quizId) {
        try {
            // Get all student attempts for a specific quiz
            const response = await axios.get(`http://localhost:8081/api/instructor/quiz/${quizId}/attempts`, getAuthHeader());
            return response.data || [];
        } catch (error) {
            console.error("Error fetching quiz attempts:", error);
            throw error;
        }
    },

    async getQuizDetails(quizId) {
        try {
            const response = await axios.get(`http://localhost:8081/api/instructor/quiz/${quizId}`, getAuthHeader());
            return response.data;
        } catch (error) {
            console.error("Error fetching quiz details:", error);
            throw error;
        }
    },

    // ---------------- NEW MANUAL GRADING ENDPOINTS ----------------
    async getPendingManualReviews() {
        try {
            console.log("🔍 Fetching pending manual reviews...");
            const response = await axios.get(`http://localhost:8081/api/instructor/analytics/pending-reviews`, getAuthHeader());
            return response.data || [];
        } catch (error) {
            console.error("Error fetching pending reviews:", error);
            throw error;
        }
    },

    async getAttemptReview(attemptId) {
        try {
            console.log(`🔍 Fetching review data for attempt ID: ${attemptId}`);
            const response = await axios.get(`http://localhost:8081/api/instructor/analytics/review/${attemptId}`, getAuthHeader());
            return response.data;
        } catch (error) {
            console.error("Error fetching attempt review:", error);
            throw error;
        }
    },

    async submitAttemptGrades(attemptId, evaluationData) {
        try {
            console.log(`📤 Submitting manual evaluation for attempt ID: ${attemptId}`, evaluationData);
            const response = await axios.post(
                `http://localhost:8081/api/instructor/analytics/review/${attemptId}`,
                evaluationData,
                getAuthHeader()
            );
            return response.data;
        } catch (error) {
            console.error("Error submitting manual review:", error);
            throw error;
        }
    }
};
