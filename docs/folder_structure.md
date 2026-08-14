# Monorepo Folder Structure Documentation: SkyVault

**System:** SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification

---

## 1. Directory Tree & Purpose Explanation

```
SkyVault/
├── apps/                               # Monorepo Applications Folder
│   ├── backend/                        # Spring Boot Core Backend Service
│   │   ├── Dockerfile                  # Production container definition
│   │   ├── pom.xml                     # Maven dependencies (Security, JPA, JJWT, Postgres)
│   │   └── src/
│   │       ├── main/java/com/skyvault/
│   │       │   ├── config/             # SecurityConfig & CORS settings
│   │       │   ├── controller/         # Auth, Aircraft, Telemetry, Blockchain, Investigation Controllers
│   │       │   ├── dto/                # Data Transfer Objects
│   │       │   ├── exception/          # GlobalExceptionHandler & custom exceptions
│   │       │   ├── mapper/             # TelemetryMapper & SHA-256 local checksums
│   │       │   ├── model/              # JPA Entities (User, Role, Aircraft, Telemetry, Block, Note)
│   │       │   ├── repository/         # Data Access Repositories
│   │       │   ├── security/           # JWT TokenProvider & Filters
│   │       │   ├── service/            # Service Business Interfaces & Implementations
│   │       │   └── util/               # CryptoHashUtils (SHA-256)
│   │       └── test/                   # JUnit 5 & Mockito test suites
│   ├── ai-service/                     # Python FastAPI AI Anomaly Service
│   │   ├── Dockerfile                  # Python 3.11-slim container definition
│   │   ├── requirements.txt            # Python dependencies (FastAPI, Pydantic, Loguru)
│   │   ├── app/
│   │   │   ├── api/v1/                 # REST API endpoints
│   │   │   ├── core/                   # Loguru logger & Settings configuration
│   │   │   ├── rules/                  # BaseAnomalyRule & 8 Anomaly Detection Rules
│   │   │   ├── schemas/                # TelemetryInput & AnomalyAlert schemas
│   │   │   └── services/               # AnomalyDetectionService & alert store
│   │   └── tests/                      # Pytest test suite
│   ├── flight-simulator/               # Flight Simulator Microservice
│   │   ├── Dockerfile                  # Spring Boot container definition
│   │   ├── pom.xml                     # Maven dependencies (Spring Retry)
│   │   └── src/main/java/com/skyvault/simulator/
│   │       ├── client/                 # RestTemplate API client with @Retryable
│   │       ├── config/                 # Simulator properties configuration
│   │       ├── engine/                 # FlightPhysicsEngine (6 flight phases)
│   │       ├── model/                  # TelemetryPayload & FlightSessionState
│   │       └── service/                # SimulationRunnerService (@Scheduled tick loop)
│   └── frontend/                       # React 18 Single-Page Application
│       ├── Dockerfile                  # Node 20 build -> Nginx Alpine production container
│       ├── package.json                # Dependencies (React, Vite, Lucide-React, Axios)
│       └── src/
│           ├── components/             # Layout (Sidebar, TopNavbar) & Common (StatCard, Badge)
│           ├── context/                # AuthContext (React Context API)
│           ├── pages/                  # Dashboard, Fleet, Live Telemetry, AI Alerts, Chain, Investigation
│           ├── services/               # Axios API clients
│           └── utils/                  # Number & date formatting utilities
├── deployment/                         # DevOps & Infrastructure Configuration
│   ├── docker-compose.yml              # Local 4-container orchestration stack
│   ├── .env.example                    # Production secrets & environment variables template
│   ├── render.yaml                     # Render Infrastructure-as-Code Blueprint
│   ├── railway.json                    # Railway deployment settings
│   └── docker/
│       └── nginx.conf                  # Production Nginx reverse proxy config
└── docs/                               # Project Documentation Suite
    ├── srs.md                          # Software Requirements Specification
    ├── sdd.md                          # Software Design Document
    ├── api_documentation.md            # REST API Specifications
    ├── user_manual.md                  # Step-by-Step User Manual
    ├── deployment_guide.md             # DevOps Deployment Guide
    ├── testing_summary.md              # Quality Assurance Test Summary
    ├── folder_structure.md             # Monorepo Folder Structure Documentation
    ├── viva_preparation_guide.md       # Viva Defense & Q&A Preparation Guide
    ├── demo_script.md                  # 5-7 Minute Project Demo Presentation Script
    └── postman/
        └── SkyVault.postman_collection.json # Exported Postman API Suite
```
