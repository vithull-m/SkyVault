import pytest
from app.schemas.telemetry import TelemetryInput
from app.rules.rules import (
    RapidAltitudeDropRule,
    EngineOverheatingRule,
    LowFuelRule,
    AbnormalAirspeedRule,
    CabinPressureLossRule,
)


@pytest.fixture
def base_telemetry():
    return TelemetryInput(
        flightId="FL-2026-0042",
        aircraftId="b4a1c5d9-7e3f-4a2b-9c8d-1e2f3a4b5c6d",
        timestamp="2026-08-14T12:00:00Z",
        flightPhase="CRUISE",
        latitude=40.6413,
        longitude=-73.7781,
        altitudeFt=33000.0,
        airspeedKts=450.0,
        headingDeg=130.0,
        verticalSpeedFpm=0.0,
        fuelLevelLbs=18000.0,
        engineRpm=7400.0,
        engineTempC=680.0,
        oatC=-51.0,
        cabinPressurePsi=11.2,
        batteryVolts=28.2,
    )


def test_rapid_altitude_drop_triggered(base_telemetry):
    rule = RapidAltitudeDropRule()
    base_telemetry.vertical_speed_fpm = -4200.0
    alert = rule.evaluate(base_telemetry)

    assert alert is not None
    assert alert.alert_type == "RAPID_ALTITUDE_DROP"
    assert alert.severity.value == "HIGH"


def test_engine_overheating_triggered(base_telemetry):
    rule = EngineOverheatingRule()
    base_telemetry.engine_temp_c = 910.0
    alert = rule.evaluate(base_telemetry)

    assert alert is not None
    assert alert.alert_type == "ENGINE_OVERHEATING"
    assert alert.severity.value == "HIGH"


def test_low_fuel_triggered(base_telemetry):
    rule = LowFuelRule()
    base_telemetry.fuel_level_lbs = 750.0
    alert = rule.evaluate(base_telemetry)

    assert alert is not None
    assert alert.alert_type == "LOW_FUEL"
    assert alert.severity.value == "CRITICAL"


def test_cabin_pressure_loss_triggered(base_telemetry):
    rule = CabinPressureLossRule()
    base_telemetry.altitude_ft = 33000.0
    base_telemetry.cabin_pressure_psi = 7.5
    alert = rule.evaluate(base_telemetry)

    assert alert is not None
    assert alert.alert_type == "CABIN_PRESSURE_LOSS"
    assert alert.severity.value == "CRITICAL"
