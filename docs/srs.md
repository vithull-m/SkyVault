# Software Requirements Specification (SRS): SkyVault

**Project Title:** SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification  
**Document Version:** 1.0.0  
**Status:** Approved & Finalized

---

## 1. Introduction

### 1.1 Purpose
This document provides the formal Software Requirements Specification (SRS) for **SkyVault**, an enterprise-grade cloud flight data recording, AI anomaly detection, and cryptographic integrity verification system.

### 1.2 Document Conventions
* **MUST / SHALL**: Mandatory functional requirements.
* **SHOULD**: Strongly recommended features.
* **MAY**: Optional capability.

---

## 2. System Objectives & Scope

### 2.1 Objectives
1. Eliminate physical black box vulnerability to physical destruction or data tampering during flight incidents.
2. Ingest real-time time-series telemetry over cloud networks.
3. Automatically flag flight hazards using an AI anomaly detection engine.
4. Cryptographically guarantee flight log immutability using an in-house blockchain hash-chain ledger.
5. Provide safety investigators with interactive flight replays and automated report generation tools.

### 2.2 Scope
SkyVault covers full-stack flight recorder operations, including simulator dynamics, backend REST ingestion, PostgreSQL storage, Python AI anomaly rules, cryptographic chain verification, and a React web dashboard.

---

## 3. Functional Requirements

### 3.1 Authentication & Security (FR-AUTH)
* **FR-AUTH-01**: The system SHALL authenticate users using JWT Bearer Tokens.
* **FR-AUTH-02**: Passwords SHALL be encrypted using BCrypt (`strength = 10`).
* **FR-AUTH-03**: User registration SHALL be restricted exclusively to System Administrators (`ROLE_ADMIN`).
* **FR-AUTH-04**: The system SHALL enforce Role-Based Access Control (RBAC) across three roles: `ROLE_ADMIN`, `ROLE_AIRLINE_OPS`, `ROLE_INVESTIGATOR`.

### 3.2 Aircraft Fleet Management (FR-AIR)
* **FR-AIR-01**: The system SHALL support CRUD operations for aircraft fleet records.
* **FR-AIR-02**: The system SHALL enforce uniqueness on aircraft registration tail numbers.

### 3.3 Flight Telemetry Ingestion (FR-TEL)
* **FR-TEL-01**: The system SHALL ingest 1-second telemetry frames containing altitude, airspeed, heading, vertical speed, fuel level, engine RPM, engine temperature, outside air temperature, cabin pressure, battery voltage, flaps, gear, and autopilot state.
* **FR-TEL-02**: The system SHALL calculate a SHA-256 local frame signature upon receiving telemetry payloads.

### 3.4 AI Anomaly Detection (FR-AI)
* **FR-AI-01**: The AI service SHALL evaluate incoming telemetry frames in real time against 8 anomaly rules: Rapid Altitude Drop, Engine Overheating, Low Fuel, Abnormal Airspeed, High Vertical Speed, Cabin Pressure Loss, Low Battery Voltage, Engine RPM Anomaly.
* **FR-AI-02**: The AI service SHALL assign severity ratings (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`) and log all triggered alerts.

### 3.5 Blockchain Integrity Verification (FR-CHAIN)
* **FR-CHAIN-01**: The system SHALL generate a cryptographic block linking each telemetry frame to the previous block via SHA-256 hash pointers (`previousHash` -> `currentHash`).
* **FR-CHAIN-02**: The system SHALL provide an automated tamper detection engine capable of detecting SQL row modifications and identifying the exact tampered block index.

### 3.6 Air Safety Investigation Portal (FR-INV)
* **FR-INV-01**: The portal SHALL enable searching flight records by Flight ID, Aircraft ID, Date Range, and Incident Classification.
* **FR-INV-02**: The portal SHALL provide a 60 FPS chronological flight telemetry replay environment.
* **FR-INV-03**: The portal SHALL support filing investigation notes (restricted to `ROLE_INVESTIGATOR`) and generating printable PDF reports.

---

## 4. Non-Functional Requirements

### 4.1 Performance (NFR-PERF)
* **NFR-PERF-01**: Telemetry ingestion REST API latency SHALL NOT exceed 100 milliseconds.
* **NFR-PERF-02**: The AI engine SHALL evaluate a telemetry frame against all 8 rules in under 15 milliseconds.

### 4.2 Security (NFR-SEC)
* **NFR-SEC-01**: All sensitive passwords SHALL be hashed with BCrypt.
* **NFR-SEC-02**: All protected endpoints SHALL validate JWT tokens and enforce RBAC authorizations.

### 4.3 Reliability & Immutability (NFR-REL)
* **NFR-REL-01**: Telemetry logs once anchored to the blockchain ledger SHALL be cryptographically tamper-evident.

---

## 5. Stakeholders Matrix

| Stakeholder | Role | System Access Level |
|:---|:---|:---|
| **System Administrator** | User management & maintenance | Full Admin Access (`ROLE_ADMIN`) |
| **Airline Operations Team** | Fleet monitoring & ingestion | Operational CRUD (`ROLE_AIRLINE_OPS`) |
| **Government Investigation Agency** | Forensic audit & incident investigation | Full Investigation & Report Rights (`ROLE_INVESTIGATOR`) |
