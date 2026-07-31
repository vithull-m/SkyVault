# SkyVault

> **AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification**

SkyVault is an enterprise-grade final-year capstone project designed to securely ingest, process, analyze, and store flight telemetry data. It leverages **AI/ML** for real-time anomaly detection and predictive flight analysis, alongside an immutable **Blockchain** ledger for cryptographic data integrity verification.

---

## 🏗 System Architecture Overview

```
                          ┌──────────────────────────┐
                          │   React (Vite) Web UI    │
                          └────────────┬─────────────┘
                                       │ REST / WS
                                       ▼
                          ┌──────────────────────────┐
                          │   Spring Boot Backend    │
                          └──────┬────────────┬──────┘
                                 │            │
             REST API (Internal) │            │ Web3 / RPC
                                 ▼            ▼
               ┌───────────────────┐        ┌───────────────────┐
               │ Python AI Service │        │ Blockchain Module │
               │ (FastAPI/PyTorch) │        │ (Ethereum/Hardhat)│
               └───────────────────┘        └───────────────────┘
```

---

## 📁 Repository Structure

- `apps/frontend/`: React single-page dashboard application.
- `apps/backend/`: Spring Boot core REST API and business logic service.
- `apps/ai-service/`: Python FastAPI service for telemetry anomaly detection.
- `blockchain/`: Smart contracts, compilation artifacts, and deployment scripts.
- `database/`: Database migration scripts (Flyway/Liquibase) and seed data.
- `deployment/`: Docker compose configurations and deployment scripts.
- `docs/`: Comprehensive project design, API specifications, and architecture diagrams.

---

## 🚀 Quick Start (Local Development)

```bash
# Clone the repository
git clone https://github.com/<your-team>/SkyVault.git

# Launch full stack using Docker Compose
cd SkyVault
docker-compose -f deployment/docker-compose.yml up --build
```
