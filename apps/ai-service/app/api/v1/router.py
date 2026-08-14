from typing import List, Optional
from fastapi import APIRouter, HTTPException, Query, status
from app.schemas.telemetry import TelemetryInput
from app.schemas.alert import AnomalyAlert, AnalyzeTelemetryResponse, ResolveAlertRequest
from app.services.anomaly_service import anomaly_service

router = APIRouter(prefix="/api/v1/ai", tags=["AI Anomaly Detection"])


@router.post("/analyze", response_model=AnalyzeTelemetryResponse, status_code=status.HTTP_200_OK)
def analyze_telemetry(telemetry: TelemetryInput):
    """
    Receives real-time flight telemetry frame from the Spring Boot backend or simulator,
    evaluates it against all registered anomaly rules, logs triggered warnings, and returns alerts.
    """
    triggered_alerts = anomaly_service.analyze_telemetry(telemetry)
    return AnalyzeTelemetryResponse(
        success=True,
        message=f"Telemetry analyzed. {len(triggered_alerts)} anomaly alert(s) triggered.",
        anomalies_detected=len(triggered_alerts),
        alerts=triggered_alerts
    )


@router.get("/alerts/active", response_model=List[AnomalyAlert], status_code=status.HTTP_200_OK)
def get_active_alerts():
    """Returns all currently active (unresolved) anomaly alerts."""
    return anomaly_service.get_active_alerts()


@router.get("/alerts/history", response_model=List[AnomalyAlert], status_code=status.HTTP_200_OK)
def get_alert_history(flight_id: Optional[str] = Query(None, alias="flightId", description="Filter by Flight ID")):
    """Returns complete historical list of detected anomaly alerts."""
    return anomaly_service.get_alert_history(flight_id=flight_id)


@router.put("/alerts/{alert_id}/resolve", response_model=AnomalyAlert, status_code=status.HTTP_200_OK)
def resolve_alert(alert_id: str, request: ResolveAlertRequest):
    """Resolves an active anomaly alert by its ID."""
    resolved_alert = anomaly_service.resolve_alert(alert_id, resolved_by=request.resolved_by)
    if not resolved_alert:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Alert with ID '{alert_id}' not found."
        )
    return resolved_alert
