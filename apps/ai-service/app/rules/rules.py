import uuid
from typing import Optional
from app.rules.base import BaseAnomalyRule
from app.schemas.telemetry import TelemetryInput
from app.schemas.alert import AnomalyAlert, SeverityEnum, AlertStatusEnum


class RapidAltitudeDropRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "RAPID_ALTITUDE_DROP"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        # Triggers if descending faster than -3500 fpm while not in LANDING/DESCENT phase,
        # or if descending faster than -6000 fpm during DESCENT phase
        if (telemetry.vertical_speed_fpm < -3500 and telemetry.flight_phase not in ["DESCENT", "LANDING"]) or \
           (telemetry.vertical_speed_fpm < -6000):
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="RAPID_ALTITUDE_DROP",
                severity=SeverityEnum.CRITICAL if telemetry.vertical_speed_fpm < -5000 else SeverityEnum.HIGH,
                description=f"Rapid altitude loss detected: Vertical Speed = {telemetry.vertical_speed_fpm:.0f} fpm at Altitude = {telemetry.altitude_ft:.0f} ft during {telemetry.flight_phase} phase.",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class EngineOverheatingRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "ENGINE_OVERHEATING"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        if telemetry.engine_temp_c > 850.0:
            severity = SeverityEnum.CRITICAL if telemetry.engine_temp_c > 920.0 else SeverityEnum.HIGH
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="ENGINE_OVERHEATING",
                severity=severity,
                description=f"Engine temperature exceedance: EGT recorded at {telemetry.engine_temp_c:.1f}°C (Threshold: 850.0°C).",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class LowFuelRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "LOW_FUEL"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        if telemetry.fuel_level_lbs < 1500.0:
            severity = SeverityEnum.CRITICAL if telemetry.fuel_level_lbs < 800.0 else SeverityEnum.MEDIUM
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="LOW_FUEL",
                severity=severity,
                description=f"Low fuel reserve warning: Remaining fuel is {telemetry.fuel_level_lbs:.0f} lbs (Threshold: 1500 lbs).",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class AbnormalAirspeedRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "ABNORMAL_AIRSPEED"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        # Over-speed (Mach/Vmo breach > 490 kts) or Stall speed during cruise (< 140 kts)
        if telemetry.airspeed_kts > 490.0:
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="ABNORMAL_AIRSPEED",
                severity=SeverityEnum.HIGH,
                description=f"Overspeed warning: Airspeed of {telemetry.airspeed_kts:.1f} kts exceeds maximum operating velocity (Vmo 490 kts).",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        elif telemetry.flight_phase == "CRUISE" and telemetry.airspeed_kts < 150.0:
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="ABNORMAL_AIRSPEED",
                severity=SeverityEnum.HIGH,
                description=f"Stall risk warning: Airspeed dropped to {telemetry.airspeed_kts:.1f} kts during CRUISE phase.",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class HighVerticalSpeedRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "HIGH_VERTICAL_SPEED"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        if abs(telemetry.vertical_speed_fpm) > 4000.0:
            severity = SeverityEnum.HIGH if abs(telemetry.vertical_speed_fpm) < 5500 else SeverityEnum.CRITICAL
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="HIGH_VERTICAL_SPEED",
                severity=severity,
                description=f"Extreme vertical motion rate detected: {telemetry.vertical_speed_fpm:+.0f} fpm.",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class CabinPressureLossRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "CABIN_PRESSURE_LOSS"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        # Unscheduled cabin pressure drop below 9.0 PSI at altitude > 10000 ft
        if telemetry.altitude_ft > 10000.0 and telemetry.cabin_pressure_psi < 9.0:
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="CABIN_PRESSURE_LOSS",
                severity=SeverityEnum.CRITICAL,
                description=f"Cabin depressurization alert: Cabin pressure dropped to {telemetry.cabin_pressure_psi:.2f} PSI at Altitude = {telemetry.altitude_ft:.0f} ft.",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class BatteryVoltageDropRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "BATTERY_VOLTAGE_DROP"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        if telemetry.battery_volts < 24.0:
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="BATTERY_VOLTAGE_DROP",
                severity=SeverityEnum.MEDIUM if telemetry.battery_volts > 22.0 else SeverityEnum.HIGH,
                description=f"Avionics electrical bus low voltage: Battery voltage registered at {telemetry.battery_volts:.2f} V (Nominal: 28.0 V).",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None


class EngineRpmAnomalyRule(BaseAnomalyRule):
    @property
    def rule_name(self) -> str:
        return "ENGINE_RPM_ANOMALY"

    def evaluate(self, telemetry: TelemetryInput) -> Optional[AnomalyAlert]:
        if telemetry.engine_rpm > 9900.0:
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="ENGINE_RPM_ANOMALY",
                severity=SeverityEnum.HIGH,
                description=f"Engine over-rev anomaly: Engine RPM exceeded max structural limit at {telemetry.engine_rpm:.0f} RPM.",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        elif telemetry.flight_phase in ["CLIMB", "CRUISE"] and telemetry.engine_rpm < 2000.0:
            return AnomalyAlert(
                alert_id=str(uuid.uuid4()),
                flight_id=telemetry.flight_id,
                aircraft_id=telemetry.aircraft_id,
                alert_type="ENGINE_RPM_ANOMALY",
                severity=SeverityEnum.CRITICAL,
                description=f"Possible engine flameout: Engine RPM dropped to {telemetry.engine_rpm:.0f} RPM during active {telemetry.flight_phase} flight phase.",
                timestamp=telemetry.timestamp,
                status=AlertStatusEnum.ACTIVE
            )
        return None
