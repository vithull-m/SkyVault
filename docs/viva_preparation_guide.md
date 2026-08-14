# Viva Preparation & Defense Guide: SkyVault

**Project Title:** SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification  
**Target Audience:** Project Author & Capstone Defense Presenters

---

## 1. Project High-Level Pitch (30 Seconds)
> "SkyVault is a cloud-native flight recorder (Black Box) system that ingests high-rate aircraft telemetry in real time. It uses an AI engine to detect 8 critical flight hazards on the fly, and anchors telemetry frames into an immutable cryptographic SHA-256 blockchain hash-chain to guarantee that post-flight recorder data can never be altered or tampered with by database administrators or unauthorized third parties."

---

## 2. Top 15 Viva Questions & Bulletproof Answers

### Q1: Why build a Cloud Flight Recorder when aircraft already have physical Black Boxes?
**Answer**: Physical black boxes can be destroyed in high-impact crashes, lost at sea, or physically stolen/tampered with. Cloud recording streams telemetry off-aircraft in real time so data is preserved instantly on secure cloud servers even if the aircraft is destroyed.

### Q2: Why did you choose a Monorepo repository structure?
**Answer**: For a multi-service capstone project (React, Spring Boot, Python FastAPI, Docker configs, SQL scripts), a monorepo provides a single source of truth, simplified version control, unified API contract synchronization, and enables spinning up the entire stack with a single `docker-compose up` command.

### Q3: Why is Spring Boot selected for the Core Backend instead of Node.js or Python?
**Answer**: Spring Boot provides enterprise-grade performance, multi-threaded concurrency for handling thousands of incoming telemetry requests per second, strict type safety, built-in Spring Security 6 stateless JWT handling, and Spring Data JPA transaction management.

### Q4: Why use Python FastAPI for the AI Engine instead of writing AI logic in Java?
**Answer**: Python is the industry standard for Artificial Intelligence and Machine Learning due to rich libraries (PyTorch, Scikit-learn, Pydantic, NumPy). FastAPI provides asynchronous execution speeds comparable to Node.js/Go with automatic OpenAPI/Swagger schema validation.

### Q5: Why build an in-house Blockchain Hash-Chain instead of deploying on Public Ethereum or Hyperledger?
**Answer**: 
1. **Performance & Throughput**: Public Ethereum processes 15 transactions per second with gas fees per transaction. SkyVault ingests 1-second flight telemetry frames; public gas fees and network latency would render high-rate telemetry recording impossible.
2. **Data Privacy**: Flight telemetry contains confidential airline operations data.
3. **Core Principles Preserved**: Our hash-chain uses the exact cryptographic principles of blockchain (SHA-256 hashing, `previousHash` pointers, Merkle trees, cascading tamper invalidation) without public network overhead.

### Q6: How does your tamper detection engine work?
**Answer**: When an auditor requests verification, the engine re-fetches the raw PostgreSQL telemetry row, re-computes its SHA-256 `recordHash`, and checks `currentBlock.previousHash == previousBlock.currentHash`. If any column value (e.g., altitude) was altered in PostgreSQL via SQL query, the newly computed hash won't match, instantly flagging `TAMPER_DETECTED` and pinpointing the exact block index.

### Q7: What are the 8 AI Anomaly Rules implemented?
**Answer**: Rapid Altitude Drop, Engine Overheating, Low Fuel, Abnormal Airspeed (Overspeed/Stall), High Vertical Speed, Cabin Pressure Loss, Low Battery Voltage, and Engine RPM Anomaly (Flameout/Over-rev).

### Q8: How is password security implemented?
**Answer**: Passwords are never stored in plain text. They are hashed using BCrypt (`BCryptPasswordEncoder`) with salt before saving to PostgreSQL.

### Q9: How do you handle JWT token validation?
**Answer**: Every incoming HTTP request passes through `JwtAuthenticationFilter`, which extracts the `Authorization: Bearer <token>` header, parses it using HMAC-SHA key, and validates token signature and expiration before setting `SecurityContextHolder`.

### Q10: How is server-side pagination handled?
**Answer**: Spring Data JPA `Pageable` and `PageRequest.of(pageNo, pageSize, sort)` fetch only the requested page of records from PostgreSQL using SQL `LIMIT` and `OFFSET`, preventing memory overflow when querying millions of telemetry records.

### Q11: What design pattern did you use for the AI Rule Engine?
**Answer**: The **Strategy Pattern** combined with a **Registry Pattern**. All rules subclass `BaseAnomalyRule`, allowing new detection algorithms to be added seamlessly without modifying existing application logic.

### Q12: How does the Flight Simulator generate realistic data?
**Answer**: `FlightPhysicsEngine` executes differential kinematic equations every second, advancing the aircraft through 6 phases (`PRE_FLIGHT`, `TAKEOFF`, `CLIMB`, `CRUISE`, `DESCENT`, `LANDING`) while updating altitude, airspeed, fuel depletion, and GPS coordinates.

### Q13: What happens if the backend API goes offline while the Flight Simulator is running?
**Answer**: The simulator client uses **Spring Retry** (`@Retryable`) with exponential backoff. It automatically retries transmission up to 3 times before triggering `@Recover` fallback logic.

### Q14: How is Nginx configured in the React container?
**Answer**: Nginx serves static compiled Vite JS/CSS assets and uses a `try_files $uri $uri/ /index.html` fallback rule for SPA client-side routing, while reverse-proxying `/api/*` HTTP calls directly to the Spring Boot container.

### Q15: What normalization form is the database in?
**Answer**: Third Normal Form (3NF). Non-key fields depend strictly on primary keys, and composite junction tables (`user_roles`) eliminate many-to-many redundancy.
