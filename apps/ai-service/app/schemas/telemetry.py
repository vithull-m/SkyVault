from enum import Enum
from typing import Optional
from pydantic import BaseModel, Field


class FlightPhase(str, Enum):
    PRE_FLIGHT = "PRE_FLIGHT"
    TAKEOFF = "TAKEOFF"
    CLIMB = "CLIMB"
    CRUISE = "CRUISE"
    DESCENT = "DESCENT"
    LANDING = "LANDING"


class TelemetryInput(BaseModel):
    flight_id: str = Field(..., alias="flightId", example="FL-2026-0042")
    aircraft_id: str = Field(..., alias="aircraftId", example="b4a1c5d9-7e3f-4a2b-9c8d-1e2f3a4b5c6d")
    timestamp: str = Field(..., example="2026-08-01T08:15:00Z")
    flight_phase: Optional[str] = Field("CRUISE", alias="flightPhase", example="CRUISE")

    # Position & Aerodynamics
    latitude: float = Field(..., example=40.6413)
    longitude: float = Field(..., example=-73.7781)
    altitude_ft: float = Field(..., alias="altitudeFt", example=33000.0)
    airspeed_kts: float = Field(..., alias="airspeedKts", example=450.0)
    heading_deg: float = Field(..., alias="headingDeg", example=130.0)
    vertical_speed_fpm: float = Field(..., alias="verticalSpeedFpm", example=0.0)

    # Propulsion & Systems
    fuel_level_lbs: float = Field(..., alias="fuelLevelLbs", example=18500.0)
    engine_rpm: float = Field(..., alias="engineRpm", example=7400.0)
    engine_temp_c: float = Field(..., alias="engineTempC", example=680.0)

    # Environment & Systems
    oat_c: float = Field(..., alias="oatC", example=-51.0)
    cabin_pressure_psi: float = Field(..., alias="cabinPressurePsi", example=11.2)
    battery_volts: float = Field(..., alias="batteryVolts", example=28.2)

    # Controls
    landing_gear_status: Optional[str] = Field("RETRACTED", alias="landingGearStatus")
    flaps_degrees: Optional[float] = Field(0.0, alias="flapsDegrees")
    autopilot_engaged: Optional[bool] = Field(True, alias="autopilotEngaged")

    class Config:
        populate_by_name = True
