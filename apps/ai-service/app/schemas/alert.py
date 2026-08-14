from enum import Enum
from typing import Optional, List
from pydantic import BaseModel, Field


class SeverityEnum(str, Enum):
    LOW = "LOW"
    MEDIUM = "MEDIUM"
    HIGH = "HIGH"
    CRITICAL = "CRITICAL"


class AlertStatusEnum(str, Enum):
    ACTIVE = "ACTIVE"
    RESOLVED = "RESOLVED"


class AnomalyAlert(BaseModel):
    alert_id: str = Field(..., description="Unique UUID identifying the alert")
    flight_id: str = Field(..., description="Target flight session ID")
    aircraft_id: str = Field(..., description="Target aircraft UUID")
    alert_type: str = Field(..., description="Anomaly classification type")
    severity: SeverityEnum = Field(..., description="Severity level: LOW, MEDIUM, HIGH, CRITICAL")
    description: str = Field(..., description="Human-readable explanation of anomaly event")
    timestamp: str = Field(..., description="ISO 8601 timestamp of anomaly detection")
    status: AlertStatusEnum = Field(AlertStatusEnum.ACTIVE, description="Current alert status: ACTIVE or RESOLVED")
    resolved_at: Optional[str] = Field(None, description="Timestamp when alert was resolved")
    resolved_by: Optional[str] = Field(None, description="Username or system resolving the alert")


class AnalyzeTelemetryResponse(BaseModel):
    success: bool
    message: str
    anomalies_detected: int
    alerts: List[AnomalyAlert]


class ResolveAlertRequest(BaseModel):
    resolved_by: str = Field("SYSTEM_ADMIN", description="User resolving the anomaly alert")
