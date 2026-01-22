<div align="center">

# 🎓 SkillForge

### AI-Driven Adaptive Learning & Exam Generator Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-6.0-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

<p align="center">
  <strong>🚀 A next-generation e-learning platform that leverages AI to personalize student learning paths and dynamically generate exams tailored to each user's skill level.</strong>
</p>

[Features](#-features) • [Tech Stack](#-tech-stack) • [Installation](#-installation) • [Usage](#-usage) • [API Documentation](#-api-documentation) • [Contributing](#-contributing)

---

</div>

## 📋 Overview

**SkillForge** is a comprehensive smart e-learning platform designed to revolutionize the educational experience. By analyzing student progress, performance trends, and learning patterns, the system recommends personalized learning resources and auto-generates custom quizzes using cutting-edge AI technology.

The platform supports two primary user roles:
- **👨‍🏫 Instructors** - Create courses, manage content, generate AI-powered quizzes, and track student performance
- **👨‍🎓 Students** - Enroll in courses, access learning materials, take adaptive quizzes, and monitor progress

---

## ✨ Features

### 🤖 AI-Powered Quiz Generation
- **Intelligent Question Generation** - Automatically create quizzes based on course content
- **Difficulty Adaptation** - Questions adapt to student skill levels (Easy, Medium, Hard)
- **Multiple Question Types** - Support for MCQs with dynamic answer shuffling

### 📊 Advanced Analytics Dashboard
- **Real-time Performance Tracking** - Monitor student progress with interactive charts
- **Quiz Analytics** - Detailed breakdown of quiz attempts, scores, and time spent
- **Performance Trends** - Historical data visualization with Recharts

### 📚 Course Management
- **Rich Content Support** - Upload and manage course materials
- **Topic Organization** - Structured course hierarchy with topics and subtopics
- **Material Types** - Support for documents, videos, and other learning resources

### 🎯 Adaptive Learning Engine
- **Personalized Recommendations** - AI-driven content suggestions based on performance
- **Skill Gap Analysis** - Identify areas needing improvement
- **Learning Path Optimization** - Customized learning journeys for each student

### 👤 User Experience
- **Modern UI/UX** - Beautiful, responsive Material-UI based interface
- **Dark/Light Theme** - Customizable appearance preferences
- **Profile Management** - Comprehensive user profiles with avatars

### 🔐 Security
- **JWT Authentication** - Secure token-based authentication
- **Role-Based Access Control** - Granular permissions for instructors and students
- **Secure API Endpoints** - Protected REST API with Spring Security

---

## 🛠 Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Core Language |
| **Spring Boot** | 3.2.2 | Application Framework |
| **Spring Security** | 6.x | Authentication & Authorization |
| **Spring Data JPA** | 3.x | Database ORM |
| **MySQL** | 8.0 | Production Database |
| **H2** | 2.x | Development/Testing Database |
| **JWT** | 0.11.5 | Token Authentication |
| **Lombok** | Latest | Code Generation |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| **React** | 18.3 | UI Library |
| **Vite** | 6.0 | Build Tool |
| **Material-UI** | 7.3 | Component Library |
| **React Router** | 7.x | Navigation |
| **Axios** | 1.13 | HTTP Client |
| **Recharts** | 3.6 | Data Visualization |
| **React Toastify** | 11.x | Notifications |

---

## 📁 Project Structure

```
SkillForge2.1/
├── 📂 BackEnd/
│   └── 📂 SkillForge_1/
│       ├── 📂 src/main/java/com/example/SkillForge_1/
│       │   ├── 📂 ai/              # AI Integration Services
│       │   ├── 📂 config/          # Application Configuration
│       │   ├── 📂 controller/      # REST API Controllers
│       │   ├── 📂 dto/             # Data Transfer Objects
│       │   ├── 📂 filter/          # Security Filters
│       │   ├── 📂 model/           # Entity Models
│       │   ├── 📂 repository/      # Database Repositories
│       │   ├── 📂 security/        # Security Configuration
│       │   └── 📂 service/         # Business Logic Services
│       └── 📄 pom.xml              # Maven Dependencies
│
├── 📂 FrontEnd-Antigravity/
│   └── 📂 Anti-start/
│       ├── 📂 src/
│       │   ├── 📂 context/         # React Context Providers
│       │   ├── 📂 services/        # API Service Layer
│       │   ├── 📄 App.jsx          # Main Application Component
│       │   ├── 📄 Login1.jsx       # Authentication Pages
│       │   ├── 📄 Register1.jsx
│       │   ├── 📄 StudentDashboard.jsx
│       │   ├── 📄 InstructorDashboard.jsx
│       │   ├── 📄 CourseList.jsx
│       │   ├── 📄 QuizAttempt.jsx
│       │   └── 📄 ...              # Other Components
│       └── 📄 package.json
│
└── 📄 README.md
```

---

## 🚀 Installation

### Prerequisites

- **Java 17** or higher
- **Node.js 18+** and npm
- **Maven 3.8+**
- **MySQL 8.0** (optional, H2 works for development)

### Backend Setup

```bash
# Navigate to backend directory
cd BackEnd/SkillForge_1

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

```bash
# Navigate to frontend directory
cd FrontEnd-Antigravity/Anti-start

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will start on `http://localhost:5173`

---

## ⚙️ Configuration

### Backend Configuration

Edit `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/skillforge
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000

# AI API Configuration (Optional)
gemini.api.key=your-gemini-api-key
```

### Frontend Configuration

Create `.env` file in the frontend directory:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

---

## 📖 Usage

### Getting Started

1. **Register** - Create an account as either an Instructor or Student
2. **Login** - Access the platform with your credentials
3. **Explore** - Navigate through the dashboard to access features

### For Instructors

1. **Create Courses** - Add new courses with descriptions and materials
2. **Manage Content** - Upload learning materials and organize topics
3. **Generate Quizzes** - Use AI to automatically generate quizzes
4. **Track Progress** - Monitor student performance through analytics

### For Students

1. **Browse Courses** - Explore available courses and enroll
2. **Access Materials** - View course content and learning resources
3. **Take Quizzes** - Attempt quizzes and receive instant feedback
4. **Track Performance** - Monitor progress through personal analytics

---

## 📡 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/register` | Register new user |
| `POST` | `/api/auth/login` | User login |

### Course Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/courses` | Get all courses |
| `POST` | `/api/courses` | Create new course |
| `GET` | `/api/courses/{id}` | Get course by ID |
| `PUT` | `/api/courses/{id}` | Update course |
| `DELETE` | `/api/courses/{id}` | Delete course |

### Quiz Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/quizzes` | Get all quizzes |
| `POST` | `/api/quizzes` | Create new quiz |
| `POST` | `/api/quizzes/generate` | AI-generate quiz |
| `POST` | `/api/quizzes/{id}/submit` | Submit quiz attempt |

### Analytics Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/analytics/student/{id}` | Get student analytics |
| `GET` | `/api/analytics/course/{id}` | Get course analytics |
| `GET` | `/api/analytics/quiz/{id}` | Get quiz analytics |

---

## 🎨 Screenshots

<div align="center">

### Login Page
*Modern, secure authentication interface*

### Student Dashboard
*Personalized learning hub with progress tracking*

### Quiz Interface
*Interactive quiz-taking experience with timer*

### Analytics Dashboard
*Comprehensive performance visualization*

</div>

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** your feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Contribution Guidelines

- Follow existing code style and conventions
- Write meaningful commit messages
- Update documentation as needed
- Add tests for new features

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Team

<div align="center">

| Role | Contributor |
|------|-------------|
| **Developer** | Samarth Kapdi |

</div>

---

## 📞 Support

If you encounter any issues or have questions:

- **Open an Issue** - [GitHub Issues](https://github.com/kavyaasuresh/SkillForge2.1--AI-Driven-ExamGenerator-Project-/issues)
- **Email** - Contact the development team

---

<div align="center">

**Made with ❤️ for the future of education**

⭐ Star this repository if you found it helpful!

</div>
