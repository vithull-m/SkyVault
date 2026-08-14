# 5-7 Minute Project Presentation & Demo Script: SkyVault

**Project Title:** SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification  
**Presenter:** Project Capstone Lead  
**Target Duration:** 6 Minutes

---

## ⏱ Timeline Breakdown

* **00:00 - 01:00 (1 Min)**: Introduction & Problem Statement
* **01:00 - 02:30 (1.5 Mins)**: Dashboard Overview & Aircraft Fleet Management
* **02:30 - 03:45 (1.15 Mins)**: Real-Time Telemetry Cockpit & Flight Simulator
* **03:45 - 04:45 (1 Min)**: AI Anomaly Detection & Blockchain Tamper Check Demonstration
* **04:45 - 06:00 (1.15 Mins)**: Forensic Investigation Case File & Official Report Export

---

## 🎙 Step-by-Step Script & Actions

### 1. Introduction (00:00 - 01:00)
* **Action**: Display the **SkyVault Dashboard** home page on `http://localhost:3000`.
* **Script**:
  > "Respected Examiners, welcome to our project presentation for **SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification**.  
  > Traditional physical flight recorders (Black Boxes) can be lost at sea or damaged during accidents. SkyVault solves this by continuously streaming 1-second flight telemetry to secure cloud servers, running real-time AI hazard analysis, and storing hashes in an immutable blockchain ledger."

---

### 2. Fleet & Session Management (01:00 - 02:30)
* **Action**: Click **Aircraft Fleet** on the left navigation menu. Show registered tail numbers (e.g. `N737SV`). Click **Flight Sessions**.
* **Script**:
  > "Here in the **Aircraft Fleet Registry**, airline operators manage registered aircraft. When a flight begins, a dedicated recording session is initialized. As shown in our **Flight Sessions** ledger, active flights stream data while completed flights remain archived for audit."

---

### 3. Live Telemetry Cockpit & Simulator (02:30 - 03:45)
* **Action**: Click **Live Telemetry**. Show live updating gauges (Altitude, Airspeed, Vertical Speed, Engine RPM, Fuel Level).
* **Script**:
  > "Let's inspect the **Live Telemetry Cockpit**. Our standalone Spring Boot Flight Simulator is streaming real-time flight dynamics every 1 second. Notice how the altitude, airspeed, engine RPM, and vertical speed gauges update dynamically as the flight transitions through phases from Takeoff to Cruise."

---

### 4. AI Anomaly & Blockchain Tamper Demo (03:45 - 04:45)
* **Action**: Click **AI Anomaly Alerts**. Highlight the `RAPID_ALTITUDE_DROP` alert card. Next, click **Blockchain Verification**.
* **Script**:
  > "Simultaneously, our Python FastAPI **AI Anomaly Detection Engine** scans every frame. Here, it has flagged a `RAPID_ALTITUDE_DROP` event with 94% model confidence.  
  > Next, let's open **Blockchain Verification**. Every telemetry frame generates a SHA-256 hash linked to prior blocks. If an attacker modifies telemetry values directly in PostgreSQL, our tamper detection engine instantly flags `TAMPER_DETECTED` and identifies the compromised block."

---

### 5. Investigation Portal & Report Export (04:45 - 06:00)
* **Action**: Click **Air Safety Investigations** ➔ Open Case File `FL-2026-0042` ➔ Show investigation notes form ➔ Click **Generate Official Report** ➔ Show printable PDF view.
* **Script**:
  > "Finally, safety officers open the **Air Safety Investigation Portal**. Here, investigators examine the flight timeline, review AI findings, verify cryptographic integrity, file official case notes, and export a complete, printable **Official Air Safety Investigation Report**.  
  > This concludes our live demonstration of SkyVault. Thank you, and we welcome your questions."
