# SkyVault

> **AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification**

SkyVault is an enterprise-grade final-year capstone project designed to securely ingest, process, analyze, and store flight telemetry data in real time. It combines high-frequency cloud flight recording (Black Box), real-time **AI/ML anomaly detection**, an immutable **cryptographic blockchain hash-chain ledger** for data tamper verification, and an **air safety investigation portal**.

---

## 🏗 System Architecture

```
                          ┌──────────────────────────┐
                          │   React 18 Dashboard     │
                          │   (Vite + Glassmorphism) │
                          └────────────┬─────────────┘
                                       │ REST / JWT
                                       ▼
                          ┌──────────────────────────┐
                          │   Spring Boot Backend    │
                          │   (Java 17 / Security)   │
                          └──────┬────────────┬──────┘
                                 │            │
             REST API (Internal) │            │ Hash Chain / SQL
                                 ▼            ▼
               ┌───────────────────┐        ┌───────────────────┐
               │ Python AI Service │        │   PostgreSQL 15   │
               │ (FastAPI/Pydantic)│        │ Database & Ledger │
               └───────────────────┘        └───────────────────┘
```

---

## ✨ Key Features

1. **JWT & Role-Based Access Control (RBAC)**: Secure access control supporting System Administrators (`ROLE_ADMIN`), Airline Operations (`ROLE_AIRLINE_OPS`), and Government Safety Investigators (`ROLE_INVESTIGATOR`).
2. **Aircraft Fleet Registry**: Complete CRUD management of physical or virtual aircraft fleet inventories.
3. **Flight Simulator Microservice**: Simulates realistic 6-phase flight dynamics (Pre-flight, Takeoff, Climb, Cruise, Descent, Landing) broadcasting 1-second telemetry frames.
4. **Time-Series Telemetry Ingestion API**: High-rate telemetry ingestion with pagination, sorting, and SHA-256 local frame checksums.
5. **AI Anomaly Detection Engine**: Rule-based AI service detecting 8 real-time flight hazards (Rapid Altitude Drop, Engine Overheating, Low Fuel, Abnormal Airspeed, High Vertical Speed, Cabin Depressurization, Low Battery, Engine RPM Anomaly).
6. **Cryptographic Blockchain Hash-Chain**: In-house blockchain ledger binding every telemetry frame to prior blocks using SHA-256 hash pointers (`previousHash` -> `currentHash`).
7. **Tamper Detection Engine**: Detects database SQL payload alterations, pinpoints tampered block height, and isolates compromised telemetry frames.
8. **Forensic Air Safety Investigation Portal**: Flight replay cockpit with timeline scrubbing slider, investigation case notes submission, and printable official safety reports.

---

## 🛠 Technology Stack

* **Frontend**: React 18, Vite, Lucide-React, Axios, Glassmorphism CSS Design System
* **Core Backend**: Java 17, Spring Boot 3.2+, Spring Security 6, JJWT, Spring Data JPA
* **AI Anomaly Service**: Python 3.11, FastAPI, Pydantic v2, Loguru, Uvicorn
* **Flight Simulator**: Spring Boot Scheduled Microservice, Spring Retry
* **Database & Ledger**: PostgreSQL 15+ (Time-Series Indexed)
* **DevOps & Containerization**: Docker, Docker Compose, Nginx, Render, Railway, AWS Architecture

---

## 🚀 Quick Start (Local Development)

### 1. Clone & Launch Stack via Docker Compose

```bash
git clone https://github.com/vithull-m/SkyVault.git
cd SkyVault
docker-compose -f deployment/docker-compose.yml up --build
```

### 2. Access Web Dashboard & APIs

* **React Web Dashboard**: `http://localhost:3000` (or `http://localhost:80`)
* **Spring Boot REST API**: `http://localhost:8080/api/v1`
* **Python AI Engine Swagger Docs**: `http://localhost:8082/docs`

---

## 📁 Repository Structure

* `apps/frontend/`: React single-page dashboard application.
* `apps/backend/`: Spring Boot core REST API, security, and blockchain logic.
* `apps/ai-service/`: Python FastAPI service for telemetry anomaly detection.
* `apps/flight-simulator/`: Standalone flight dynamics simulator engine.
* `deployment/`: Docker compose, Nginx configs, `.env` templates, Render & Railway setups.
* `docs/`: Comprehensive SRS, SDD, API docs, User Manual, Testing Summary, Viva Guide & Demo Script.

---

## 📑 Project Documentation Index

1. 📄 [Software Requirements Specification (SRS)](file:///d:/SkyVault/docs/srs.md)
2. 📄 [Software Design Document (SDD)](file:///d:/SkyVault/docs/sdd.md)
3. 📄 [API Documentation](file:///d:/SkyVault/docs/api_documentation.md)
4. 📄 [End-User Manual](file:///d:/SkyVault/docs/user_manual.md)
5. 📄 [Deployment & DevOps Guide](file:///d:/SkyVault/docs/deployment_guide.md)
6. 📄 [Testing & Quality Assurance Summary](file:///d:/SkyVault/docs/testing_summary.md)
7. 📄 [Project Folder Structure Guide](file:///d:/SkyVault/docs/folder_structure.md)
8. 🎓 [Viva Preparation & Defense Guide](file:///d:/SkyVault/docs/viva_preparation_guide.md)
9. 🎬 [Project Demo Script (5-7 Mins)](file:///d:/SkyVault/docs/demo_script.md)

---

## 👥 Contributors & License

* **Project Author**: Senior Full-Stack Engineering Team
* **License**: MIT License - Free for Academic Capstone & Project Evaluation.
