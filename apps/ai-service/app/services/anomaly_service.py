from datetime import datetime, timezone
from typing import List, Dict, Optional
from loguru import logger
from app.rules.registry import rule_registry
from app.schemas.telemetry import TelemetryInput
from app.schemas.alert import AnomalyAlert, AlertStatusEnum


class AnomalyDetectionService:
    """
    Service responsible for telemetry evaluation, logging alerts, and maintaining alert state.
    """

    def __init__(self):
        # In-memory alert store keyed by alert_id
        self._alerts_db: Dict[str, AnomalyAlert] = {}

    def analyze_telemetry(self, telemetry: TelemetryInput) -> List[AnomalyAlert]:
        """
        Evaluates incoming telemetry payload, logs any detected anomalies, stores alerts, and returns them.
        """
        alerts = rule_registry.evaluate_all(telemetry)

        for alert in alerts:
            self._alerts_db[alert.alert_id] = alert
            logger.warning(
                f"🚨 [AI ANOMALY DETECTED] AlertID={alert.alert_id} | "
                f"Flight={alert.flight_id} | Aircraft={alert.aircraft_id} | "
                f"Type={alert.alert_type} | Severity={alert.severity.value} | "
                f"Desc='{alert.description}'"
            )

        return alerts

    def get_active_alerts(self) -> List[AnomalyAlert]:
        """Returns all currently active (unresolved) alerts."""
        return [alert for alert in self._alerts_db.values() if alert.status == AlertStatusEnum.ACTIVE]

    def get_alert_history(self, flight_id: Optional[str] = None) -> List[AnomalyAlert]:
        """Returns historical alerts, optionally filtered by flight_id."""
        alerts = list(self._alerts_db.values())
        if flight_id:
            alerts = [a for a in alerts if a.flight_id == flight_id]
        return sorted(alerts, key=lambda x: x.timestamp, reverse=True)

    def resolve_alert(self, alert_id: str, resolved_by: str = "SYSTEM_ADMIN") -> Optional[AnomalyAlert]:
        """Marks an alert as RESOLVED."""
        alert = self._alerts_db.get(alert_id)
        if alert:
            alert.status = AlertStatusEnum.RESOLVED
            alert.resolved_at = datetime.now(timezone.utc).isoformat()
            alert.resolved_by = resolved_by
            logger.info(f"✅ [ALERT RESOLVED] AlertID={alert_id} resolved by '{resolved_by}'")
            return alert
        return None


# Global singleton instance
anomaly_service = AnomalyDetectionService()
