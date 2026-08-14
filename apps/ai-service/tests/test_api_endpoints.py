from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_health_check_endpoint():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "HEALTHY"


def test_analyze_telemetry_endpoint():
    payload = {
        "flightId": "FL-2026-0042",
        "aircraftId": "b4a1c5d9-7e3f-4a2b-9c8d-1e2f3a4b5c6d",
        "timestamp": "2026-08-14T12:00:00Z",
        "flightPhase": "CRUISE",
        "latitude": 40.6413,
        "longitude": -73.7781,
        "altitudeFt": 33000.0,
        "airspeedKts": 450.0,
        "headingDeg": 130.0,
        "verticalSpeedFpm": -4500.0,  # Should trigger Rapid Altitude Drop
        "fuelLevelLbs": 18000.0,
        "engineRpm": 7400.0,
        "engineTempC": 680.0,
        "oatC": -51.0,
        "cabinPressurePsi": 11.2,
        "batteryVolts": 28.2
    }

    response = client.post("/api/v1/ai/analyze", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["success"] is True
    assert data["anomalies_detected"] >= 1
    assert data["alerts"][0]["alert_type"] == "RAPID_ALTITUDE_DROP"


def test_get_active_alerts_endpoint():
    response = client.get("/api/v1/ai/alerts/active")
    assert response.status_code == 200
    assert isinstance(response.json(), list)
