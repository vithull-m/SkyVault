# End-User Manual: SkyVault

**System:** SkyVault – AI-Based Intelligent Cloud Flight Recorder with Blockchain-Based Data Integrity Verification  
**Audience:** System Administrators, Airline Operations Officers, Government Safety Investigators

---

## 1. User Login
1. Open your web browser and navigate to `http://localhost:3000`.
2. On the Top Navigation Bar, verify your active session avatar (`AJ` - `Capt. Alex Johnson`).
3. Your default permission scope (`ROLE_ADMIN` or `ROLE_INVESTIGATOR`) dictates accessible menu items.

---

## 2. Registering an Aircraft
1. Click **Aircraft Fleet** on the left sidebar navigation menu.
2. Click the blue **+ Register Aircraft** button at the top right.
3. Fill in required parameters:
   * **Registration Tail Number**: e.g., `N737SV`
   * **Aircraft Model**: e.g., `Boeing 737-800`
   * **Manufacturer**: e.g., `Boeing`
   * **Airline Name**: e.g., `SkyVault Airways`
   * **Manufacturing Year**: e.g., `2020`
   * **Capacity**: e.g., `189`
   * **Engine Type**: e.g., `CFM56-7B`
   * **Status**: `ACTIVE`
4. Click **Save Aircraft**. The new tail number will immediately appear in the fleet registry.

---

## 3. Starting a Flight Simulation Stream
1. Open a terminal and navigate to `apps/flight-simulator`.
2. Run `mvn spring-boot:run` or execute `docker-compose up skyvault-ai skyvault-backend skyvault-simulator`.
3. The simulator will initialize `FL-2026-0042` and begin broadcasting 1-second telemetry frames across 6 flight phases (`PRE_FLIGHT` ➔ `TAKEOFF` ➔ `CLIMB` ➔ `CRUISE` ➔ `DESCENT` ➔ `LANDING`).

---

## 4. Viewing the Real-Time Cockpit Dashboard
1. Click **Live Telemetry** on the left sidebar.
2. Observe 10 real-time instrument gauges updating every second:
   * Altitude (ft), Airspeed (kts), Heading (°), Vertical Speed (fpm), Fuel (lbs), Engine RPM, EGT (°C), Cabin Pressure (PSI), OAT (°C), Battery Voltage (V).
3. Use the **Pause Stream** / **Resume Stream** button to halt live updates for inspection.

---

## 5. Inspecting AI Anomaly Alerts
1. Click **AI Anomaly Alerts** on the sidebar.
2. View detected flight hazard cards categorized by severity (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`).
3. Click **Review** on any alert to open detailed feature attribution logs.

---

## 6. Verifying Blockchain Data Integrity
1. Click **Blockchain Verification** on the sidebar.
2. Review the list of anchored telemetry blocks and Merkle tree root hashes.
3. If database tampering has occurred in PostgreSQL, the status badge will flag `⚠ TAMPER DETECTED` in red and identify the exact tampered block number.

---

## 7. Generating an Official Air Safety Report
1. Click **Air Safety Investigations** on the sidebar.
2. Select target case file (e.g., `FL-2026-0042`).
3. Enter your forensic audit notes in the **Investigation Case Notes** box and click **Save Note**.
4. Click **Generate Official Report** at the top right.
5. Review the compiled report document and click **Print / Export Official PDF Report**.
