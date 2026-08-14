# API Documentation: SkyVault

**Base URL (Spring Boot Backend):** `http://localhost:8080/api/v1`  
**Base URL (Python AI Service):** `http://localhost:8082/api/v1/ai`

---

## 1. Authentication Endpoints

### 1.1 User Login
* **URL:** `POST /auth/login`
* **Access:** Public
* **Request Body:**
```json
{
  "usernameOrEmail": "admin_user",
  "password": "AdminSecret123!"
}
```
* **Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Authentication successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "userId": "b4a1c5d9-7e3f-4a2b-9c8d-1e2f3a4b5c6d",
    "username": "admin_user",
    "roles": ["ROLE_ADMIN"]
  }
}
```

---

## 2. Aircraft Management Endpoints

### 2.1 Add Aircraft
* **URL:** `POST /aircraft`
* **Access:** `ROLE_ADMIN`, `ROLE_AIRLINE_OPS`
* **Request Body:**
```json
{
  "registrationNumber": "N737SV",
  "model": "Boeing 737-800",
  "manufacturer": "Boeing",
  "airlineName": "SkyVault Airways",
  "manufacturingYear": 2020,
  "capacity": 189,
  "engineType": "CFM56-7B",
  "status": "ACTIVE"
}
```
* **Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Aircraft created successfully",
  "data": {
    "id": "c5b2a1d9-8e4f-4b3c-9d0e-2f3a4b5c6d7e",
    "registrationNumber": "N737SV",
    "status": "ACTIVE"
  }
}
```

---

## 3. Flight Telemetry Endpoints

### 3.1 Ingest Telemetry Frame
* **URL:** `POST /telemetry/ingest`
* **Access:** `ROLE_ADMIN`, `ROLE_AIRLINE_OPS`
* **Request Body:**
```json
{
  "flightId": "FL-2026-0042",
  "aircraftId": "b4a1c5d9-7e3f-4a2b-9c8d-1e2f3a4b5c6d",
  "timestamp": "2026-08-14T12:00:00Z",
  "flightPhase": "CRUISE",
  "latitude": 40.6413,
  "longitude": -73.7781,
  "altitudeFt": 33000.0,
  "airspeedKts": 450.0,
  "headingDeg": 130.0,
  "verticalSpeedFpm": 0.0,
  "fuelLevelLbs": 18500.0,
  "engineRpm": 7400.0,
  "engineTempC": 680.0,
  "oatC": -51.0,
  "cabinPressurePsi": 11.2,
  "batteryVolts": 28.2,
  "landingGearStatus": "RETRACTED",
  "flapsDegrees": 0.0,
  "autopilotEngaged": true
}
```
* **Response (201 Created):** Returns telemetry object with SHA-256 signature.

---

## 4. Blockchain Integrity Endpoints

### 4.1 Verify Flight Cryptographic Chain
* **URL:** `GET /blockchain/verify/flight/{flightId}`
* **Access:** `ROLE_ADMIN`, `ROLE_AIRLINE_OPS`, `ROLE_INVESTIGATOR`
* **Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Flight cryptographic chain verification completed",
  "data": {
    "flightId": "FL-2026-0042",
    "totalBlocksAnalyzed": 18432,
    "isChainValid": true,
    "status": "VERIFIED"
  }
}
```

---

## 5. AI Anomaly Engine Endpoints

### 5.1 Real-Time Telemetry Analysis
* **URL:** `POST /api/v1/ai/analyze` (Port 8082)
* **Access:** Internal / Public Service
* **Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Telemetry analyzed. 1 anomaly alert(s) triggered.",
  "anomalies_detected": 1,
  "alerts": [
    {
      "alert_id": "8f3b2a1c-...",
      "alert_type": "RAPID_ALTITUDE_DROP",
      "severity": "HIGH",
      "description": "Rapid altitude loss detected: Vertical Speed = -4200 fpm."
    }
  ]
}
```
