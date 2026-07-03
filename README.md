# SkillForge – AI-Driven Adaptive Learning Exam Generator

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Build-Maven-blue?style=for-the-badge&logo=apachemaven" />
  <img src="https://img.shields.io/badge/Status-Production%20Ready-success?style=for-the-badge" />
</p>

<p align="center">
  <b>An intelligent Spring Boot platform for adaptive learning, personalized assessments, and AI-powered exam generation.</b>
</p>

---

## 📚 Overview

**SkillForge** is a full-stack **Java Spring Boot** application designed to improve student outcomes with **adaptive assessments** and **AI-generated exams**.

It analyzes learner performance, adjusts question difficulty dynamically, and provides targeted feedback to strengthen weak areas.

---

## ✨ Key Features

- 🧠 **AI-Powered Question Generation**
- 🎯 **Adaptive Difficulty Adjustment**
- 📝 **Custom Exam Creation by Topic & Level**
- 📊 **Student Performance Analytics**
- 🔁 **Reattempt & Weak-Area Reinforcement**
- 👨‍🏫 **Instructor/Admin Controls**
- 🔐 **Secure Authentication & Authorization**

---

## 🏗️ Tech Stack

- **Language:** Java  
- **Framework:** Spring Boot  
- **Build Tool:** Maven  
- **Database:** (Add your DB here: MySQL/PostgreSQL/H2)  
- **Security:** Spring Security + JWT (if implemented)  
- **API Style:** RESTful APIs  
- **Templating/Frontend:** (Add Thymeleaf/React/Angular if applicable)

---

## 📁 Project Structure

```bash
SkillForge-AI-Driven-Adaptive-Learning-Exam-Generator/
├── src/
│   ├── main/
│   │   ├── java/com/skillforge/
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── model/           # Entities / domain models
│   │   │   ├── dto/             # Request/response DTOs
│   │   │   ├── config/          # Security & app configuration
│   │   │   └── SkillForgeApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/                    # Unit & integration tests
├── pom.xml
└── README.md
```

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/SamarthKapdi/SkillForge-AI-Driven-Adaptive-Learning-Exam-Generator.git
cd SkillForge-AI-Driven-Adaptive-Learning-Exam-Generator
```

### 2. Configure Environment

Update `src/main/resources/application.properties`:

```properties
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/skillforge
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT (if applicable)
app.jwt.secret=your_secret_key
app.jwt.expiration=86400000
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

App will start at:  
👉 `http://localhost:8080`

---

## 🔌 API Endpoints (Sample)

> Update these based on your actual controllers.

- `POST /api/auth/register` – Register user  
- `POST /api/auth/login` – Login user  
- `POST /api/exams/generate` – Generate adaptive exam  
- `GET /api/exams/{id}` – Get exam details  
- `POST /api/exams/{id}/submit` – Submit answers  
- `GET /api/analytics/student/{id}` – Student performance analytics  

---

## 🧪 Testing

Run tests using:

```bash
mvn test
```

---

## 📈 Adaptive Engine Workflow

1. Student selects course/topic  
2. System evaluates current proficiency  
3. AI generates personalized question paper  
4. Student submits responses  
5. Engine analyzes score + weak areas  
6. Next exam adapts difficulty and focus topics

---

## 🔒 Security

- Role-based access (Admin / Teacher / Student)
- JWT-based authentication (if enabled)
- Protected API routes
- Input validation and exception handling

---

## 🛣️ Future Enhancements

- [ ] AI explanation for each answer
- [ ] Question tagging with Bloom’s taxonomy
- [ ] Time-based adaptive mock tests
- [ ] Leaderboard and gamification
- [ ] Email/report export for progress

---

## 🤝 Contributing

Contributions are welcome!

1. Fork this repository  
2. Create your feature branch (`feature/amazing-feature`)  
3. Commit changes (`git commit -m "Add amazing feature"`)  
4. Push to branch (`git push origin feature/amazing-feature`)  
5. Open a Pull Request

---

## 👨‍💻 Author

**Samarth Kapdi**  
GitHub: [@SamarthKapdi](https://github.com/SamarthKapdi)

---

## ⭐ Show Your Support

If you like this project, give it a **star** on GitHub!

---

## 📄 License

This project is licensed under the **MIT License**.
