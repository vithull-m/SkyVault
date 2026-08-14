from typing import List, Type
from loguru import logger
from app.rules.base import BaseAnomalyRule
from app.rules.rules import (
    RapidAltitudeDropRule,
    EngineOverheatingRule,
    LowFuelRule,
    AbnormalAirspeedRule,
    HighVerticalSpeedRule,
    CabinPressureLossRule,
    BatteryVoltageDropRule,
    EngineRpmAnomalyRule,
)
from app.schemas.telemetry import TelemetryInput
from app.schemas.alert import AnomalyAlert


class RuleEngineRegistry:
    """
    Registry pattern for rule discovery and evaluation.
    New anomaly rules can be added effortlessly by registering them in this class.
    """

    def __init__(self):
        self._rules: List[BaseAnomalyRule] = []

    def register_rule(self, rule: BaseAnomalyRule) -> None:
        self._rules.append(rule)
        logger.info(f"Loaded AI Anomaly Rule: [{rule.rule_name}]")

    def evaluate_all(self, telemetry: TelemetryInput) -> List[AnomalyAlert]:
        """
        Evaluates a telemetry frame against all registered rules in sequence.
        Returns a list of all triggered AnomalyAlert instances.
        """
        triggered_alerts: List[AnomalyAlert] = []
        for rule in self._rules:
            try:
                alert = rule.evaluate(telemetry)
                if alert:
                    triggered_alerts.append(alert)
            except Exception as ex:
                logger.error(f"Error evaluating rule [{rule.rule_name}]: {str(ex)}")
        return triggered_alerts


# Instantiate and auto-register initial default rules
rule_registry = RuleEngineRegistry()
rule_registry.register_rule(RapidAltitudeDropRule())
rule_registry.register_rule(EngineOverheatingRule())
rule_registry.register_rule(LowFuelRule())
rule_registry.register_rule(AbnormalAirspeedRule())
rule_registry.register_rule(HighVerticalSpeedRule())
rule_registry.register_rule(CabinPressureLossRule())
rule_registry.register_rule(BatteryVoltageDropRule())
rule_registry.register_rule(EngineRpmAnomalyRule())
