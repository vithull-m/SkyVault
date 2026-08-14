from abc import ABC, abstractmethod
from typing import Optional
from app.schemas.telemetry import TelemetryInput
from app.schemas.alert import AnomalyAlert


class BaseAnomalyRule(ABC):
    """
    Abstract Base Class for all AI Anomaly Rules.
    Every new anomaly detection rule must inherit from this class
    and implement the evaluate() method.
    """

    @property
    @abstractmethod
    def rule_name(self) -> str:
        """Unique identifier name for this rule."""
        pass

    @abstractmethod
    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        """
        Evaluates a single telemetry payload frame.
        Returns an AnomalyAlert if rule condition is violated, or None if normal.
        """
        pass
