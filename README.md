<div align="center">

<img src="https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring%20AI-2.0.0--M2-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>
<img src="https://img.shields.io/badge/PostgreSQL-18.x-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
<img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/Railway-Deployed-0B0D0E?style=for-the-badge&logo=railway&logoColor=white"/>

# 🎓 Career-OS

### *AI-Powered Campus Placement Management System*

A production-grade backend for end-to-end campus recruitment — built with Spring Boot 4, Spring AI, JWT authentication, and a GPT-powered ATS resume scoring engine.

[Features](#-features) • [Architecture](#-architecture) • [API Reference](#-api-reference) • [Getting Started](#-getting-started) • [Deployment](#-deployment)

</div>

---

## 📌 Overview

CareerOS is a full-stack placement management backend that connects **students**, **companies**, and **admins** through a structured hiring pipeline. It features secure JWT-based auth with refresh token rotation, full CRUD for job postings and applications, resume management, and an **AI-powered ATS scoring engine** using Spring AI + OpenRouter (GPT-4o-mini).

Built to demonstrate real-world backend engineering — not a tutorial project.

---

## ✨ Features

### 🔐 Authentication & Security
- JWT access tokens + refresh token rotation (stateless, CSRF-disabled)
- Role-based access control: `STUDENT`, `COMPANY`, `ADMIN`
- Custom `JwtAuthFilter` with `OncePerRequestFilter`
- Secure logout with refresh token invalidation

### 👤 User Management
- Student profiles: skills, CGPA, branch, graduation year, college
- Company profiles: industry, location, verification status
- Profile completion tracking

### 💼 Job Lifecycle
- Companies post jobs with salary range, type, required skills, openings, deadline
- Students apply to open jobs (duplicate application prevention via DB unique constraint)
- Full application status pipeline: `APPLIED → UNDER_REVIEW → SHORTLISTED → REJECTED → HIRED`

### 📄 Resume Management
- PDF-only upload with 5MB size cap
- UUID-prefixed file naming to prevent collisions
- Binary download with proper `Content-Disposition` headers
- Auto-replace on re-upload (one resume per student)

### 🤖 AI-Powered ATS Scoring
- Resume text extraction via Apache PDFBox 3.x
- GPT-4o-mini analysis via OpenRouter through Spring AI's `ChatClient`
- Structured JSON scoring: overall, skills, experience, education, formatting
- Missing keywords, actionable suggestions, and strengths returned per report
- Full ATS history per resume stored in PostgreSQL

### 📚 Developer Experience
- Swagger UI with JWT Bearer auth support
- Global exception handler (validation, bad request, server errors)
- CORS pre-configured for `localhost:3000` and Vercel frontend
- MapStruct DTO mapping (zero boilerplate conversions)

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Client (Next.js)                  │
└───────────────────────┬─────────────────────────────┘
                        │ HTTPS / REST
┌───────────────────────▼─────────────────────────────┐
│              Spring Boot Backend (Railway)           │
│                                                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐   │
│  │  Auth    │  │  Job /   │  │   Resume / ATS   │   │
│  │  Module  │  │  Apply   │  │     Module       │   │
│  └────┬─────┘  └────┬─────┘  └────────┬─────────┘   │
│       │              │                 │              │
│  ┌────▼──────────────▼─────────────────▼──────────┐  │
│  │         Service Layer + MapStruct DTOs          │  │
│  └─────────────────────────┬───────────────────────┘  │
│                             │                          │
│  ┌──────────────────────────▼───────────────────────┐ │
│  │    JPA / Hibernate 7  ←→  PostgreSQL             │ │
│  └──────────────────────────────────────────────────┘ │
│                                                        │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Spring AI ChatClient → OpenRouter → GPT-4o-mini│  │
│  └─────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────┘
```

### Package Structure

```
com.K955.Placement_Portal
├── config/          # Security, Swagger, AI, CORS
├── controller/      # REST controllers
├── dto/             # Request/Response DTOs
├── exception/       # GlobalExceptionHandler, custom exceptions
├── mapper/          # MapStruct interfaces
├── model/
│   ├── entity/      # JPA entities
│   └── enums/       # Role, JobType, ApplicationStatus, JobPostingStatus
├── repository/      # Spring Data JPA interfaces
├── security/        # JwtUtil, JwtAuthFilter, CustomUserDetailsService
├── service/
│   ├── interfaces/  # Service contracts
│   └── impl/        # Business logic implementations
└── util/            # FileStorageUtil, PdfTextExtractor
```

---

## 🗃 Data Model

```
User ──────────── Student ─────── Resume ──── AtsReport
  │                                  
  └────────────── Company
  
JobPosting ──── Application ──── Student
  │
  └──── Company
```

| Entity | Key Fields |
|---|---|
| `User` | id, email (unique), password (BCrypt), role |
| `Student` | cgpa, skills[], college, branch, graduationYear, profileComplete |
| `Company` | companyName (unique), industry, verified |
| `JobPosting` | title, jobType, salary range, requiredSkills[], status, deadline |
| `Application` | status pipeline, unique(student + job) constraint |
| `Resume` | fileName, filePath, fileType, fileSize |
| `AtsReport` | overallScore, skillScore, experienceScore, missingKeywords[], suggestions[] |
| `RefreshToken` | token (unique), expiryDate |

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 14+
- An [OpenRouter](https://openrouter.ai) API key

### 1. Clone the repo

```bash
git clone https://github.com/krisharma955/SmartPlacement-Portal.git
cd SmartPlacement-Portal
```

### 2. Configure environment

Create `src/main/resources/application-local.yml` or set environment variables:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/placement_portal
    username: your_db_user
    password: your_db_password

  ai:
    openai:
      api-key: your_openrouter_api_key
      base-url: https://openrouter.ai/api
      chat:
        options:
          model: openai/gpt-4o-mini
          temperature: 0.3

app:
  jwt:
    secret: your_256bit_secret_key
  upload-dir: uploads/resumes
```

### 3. Run

```bash
mvn spring-boot:run
```

App starts at `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 📡 API Reference

### Auth — `/api/auth`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `POST` | `/signup` | Register student or company | ❌ |
| `POST` | `/login` | Login, returns access + refresh tokens | ❌ |
| `POST` | `/refresh` | Rotate refresh token | ❌ |
| `POST` | `/logout` | Invalidate refresh token | ✅ |

### Students — `/api/students`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/profile` | Get own profile |
| `PATCH` | `/profile` | Update profile + skills |

### Companies — `/api/companies`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/profile` | Get own company profile |
| `PATCH` | `/profile` | Update company details |

### Jobs — `/api/jobs`

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| `GET` | `/` | List all open jobs | ❌ |
| `GET` | `/{id}` | Get job details | ❌ |
| `POST` | `/` | Post a new job | ✅ Company |
| `PUT` | `/{id}` | Update job posting | ✅ Company |
| `DELETE` | `/{id}` | Delete job posting | ✅ Company |

### Applications — `/api/applications`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/` | Apply to a job |
| `GET` | `/student` | My applications |
| `GET` | `/company` | Applications received |
| `PATCH` | `/{id}/status` | Update application status |

### Resume — `/api/resume`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/upload` | Upload PDF resume (5MB max) |
| `GET` | `/` | Get resume metadata |
| `GET` | `/download` | Download resume binary |

### ATS — `/api/resume/analyze`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/` | Run AI ATS analysis on latest resume |
| `GET` | `/history` | All past ATS reports |
| `GET` | `/{reportId}` | Get specific ATS report |

---

## ☁️ Deployment

This project is deployed on **Railway**.

### Environment Variables (Railway)

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `OPENAI_API_KEY` | OpenRouter API key |
| `OPENAI_BASE_URL` | `https://openrouter.ai/api` |
| `OPENAI_MODEL` | `openai/gpt-4o-mini` |
| `JWT_SECRET` | 256-bit secret string |
| `FILE_UPLOAD_DIR` | `uploads/resumes` |

> ⚠️ **Note:** Railway has an ephemeral filesystem. Uploaded resumes are wiped on redeploy. A migration to **Cloudinary or AWS S3** is planned for production.

---

## 🛣 Roadmap

- [ ] S3 / Cloudinary integration for persistent file storage
- [ ] Admin dashboard APIs (verify companies, manage users)
- [ ] Email notifications (application status updates)
- [ ] Pagination + filtering on job listings
- [ ] RAG pipeline — AI answers questions about uploaded resumes
- [ ] Frontend deployment to Vercel

---

## 🧰 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Security | Spring Security 7, JJWT 0.12.6 |
| AI | Spring AI 2.0.0-M2, OpenRouter, GPT-4o-mini |
| Database | PostgreSQL 18, Spring Data JPA, Hibernate 7 |
| Mapping | MapStruct |
| PDF | Apache PDFBox 3.0.3 |
| Docs | SpringDoc OpenAPI 2.8.0 (Swagger UI) |
| Build | Maven |
| Deploy | Railway |

---

## 👤 Author

**Krish Sharma**  
[GitHub](https://github.com/krisharma955) • [LinkedIn](https://linkedin.com/in/krisharma955)

---

<div align="center">

*Built from scratch*

</div>
