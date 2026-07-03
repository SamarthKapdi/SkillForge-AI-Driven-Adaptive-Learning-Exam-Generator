# SkillForge: AI-Driven Adaptive Learning & Exam Generator

<p align="center">
  <img alt="SkillForge Banner" src="https://img.shields.io/badge/SkillForge-AI%20Adaptive%20Learning-blueviolet?style=for-the-badge" />
  <img alt="License" src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" />
  <img alt="Status" src="https://img.shields.io/badge/Status-Active%20Development-orange?style=for-the-badge" />
</p>

<p align="center">
  <b>Personalized learning. Intelligent assessments. Actionable feedback.</b><br/>
  SkillForge helps learners master concepts through adaptive exam generation powered by AI.
</p>

---

## ✨ Overview

**SkillForge** is an AI-powered adaptive learning platform that dynamically creates quizzes and exams based on:
- learner skill level
- topic performance trends
- previous mistakes
- difficulty progression

Instead of one-size-fits-all testing, SkillForge delivers a personalized assessment journey to improve retention, confidence, and outcomes.

---

## 🚀 Core Features

- 🧠 **AI-Generated Questions**  
  Generate high-quality questions from topics, concepts, or learning materials.

- 🎯 **Adaptive Difficulty Engine**  
  Automatically adjusts question complexity based on learner performance.

- 📊 **Performance Analytics**  
  Track strengths, weaknesses, accuracy trends, and topic mastery.

- 📝 **Custom Exam Builder**  
  Build exams by subject, difficulty, duration, and question type.

- 🔁 **Smart Reattempt System**  
  Reintroduces weak-concept questions for reinforcement learning.

- 👩‍🏫 **Educator-Friendly Controls**  
  Configure exams, monitor learner progress, and fine-tune question pools.

---

## 🏗️ Tech Stack

Based on repository language composition:

- **JavaScript (71%)** – frontend/backend logic, UI interactions, API integration
- **Java (28.7%)** – core services, exam logic, adaptive evaluation components
- **Other (0.3%)**

> You can customize this section further with exact frameworks (e.g., React, Node.js, Spring Boot) used in your codebase.

---

## 📂 Project Structure

```bash
SkillForge-AI-Driven-Adaptive-Learning-Exam-Generator/
├── frontend/          # User interface / learner & educator dashboards
├── backend/           # APIs, business logic, exam orchestration
├── ai-engine/         # Adaptive generation & scoring logic
├── docs/              # Architecture docs, API references, diagrams
├── tests/             # Unit/integration tests
└── README.md
```

> Update folders above to match your actual repository structure.

---

## ⚙️ Getting Started

### 1) Clone the repository
```bash
git clone https://github.com/SamarthKapdi/SkillForge-AI-Driven-Adaptive-Learning-Exam-Generator.git
cd SkillForge-AI-Driven-Adaptive-Learning-Exam-Generator
```

### 2) Configure environment
Create a `.env` file in the relevant service directories and add required variables:

```env
OPENAI_API_KEY=your_api_key_here
DATABASE_URL=your_database_url
JWT_SECRET=your_jwt_secret
```

### 3) Install dependencies

#### JavaScript services
```bash
npm install
```

#### Java services
```bash
# If Maven
mvn clean install

# If Gradle
gradle build
```

### 4) Run the project

#### Start JavaScript app
```bash
npm run dev
```

#### Start Java service
```bash
mvn spring-boot:run
# or
gradle bootRun
```

---

## 🧪 Testing

```bash
# JavaScript tests
npm test

# Java tests
mvn test
# or
gradle test
```

---

## 📈 Adaptive Learning Flow

1. Learner selects topic or learning goal  
2. SkillForge estimates proficiency level  
3. AI generates a personalized question set  
4. Learner submits responses  
5. Engine evaluates performance and updates learner profile  
6. Next assessment adapts in real time

---

## 🔐 Security & Privacy

- Secure authentication and authorization
- Environment-based secret management
- Role-based access for learners and educators
- Privacy-conscious handling of learner performance data

---

## 🛣️ Roadmap

- [ ] Question quality scoring with explainability
- [ ] Multi-language exam generation
- [ ] Voice-enabled quiz mode
- [ ] Real-time proctoring integration
- [ ] LMS integration (Google Classroom, Moodle, Canvas)

---

## 🤝 Contributing

Contributions are welcome!  
If you’d like to improve SkillForge:

1. Fork the repository
2. Create a feature branch (`feature/your-feature-name`)
3. Commit changes
4. Open a pull request

---

## 👤 Author

**Samarth Kapdi**  
GitHub: [@SamarthKapdi](https://github.com/SamarthKapdi)

---

## 📄 License

This project is licensed under the **MIT License**.  
Add a `LICENSE` file if you haven’t already.

---

## 🌟 Support

If you find this project helpful, please consider giving it a ⭐ on GitHub — it helps others discover the project!
