import React, { useState, useEffect, useRef } from 'react';
import LoadingSpinner from '../components/common/LoadingSpinner';
import Badge from '../components/common/Badge';
import { Activity, RefreshCw, Wifi } from 'lucide-react';
import { formatNumber, formatTimestamp } from '../utils/formatters';

const generateMockFrame = (prevFrame, elapsed) => {
  const phases = ['PRE_FLIGHT', 'TAKEOFF', 'CLIMB', 'CRUISE', 'DESCENT', 'LANDING'];
  let phase = 'CRUISE';
  if (elapsed < 15) phase = 'PRE_FLIGHT';
  else if (elapsed < 45) phase = 'TAKEOFF';
  else if (elapsed < 120) phase = 'CLIMB';
  else if (elapsed < 240) phase = 'CRUISE';
  else if (elapsed < 320) phase = 'DESCENT';
  else phase = 'LANDING';

  const alt = prevFrame ? Math.max(0, prevFrame.altitudeFt + (Math.random() - 0.48) * 80) : 12.0;
  const spd = prevFrame ? Math.min(480, Math.max(0, prevFrame.airspeedKts + (Math.random() - 0.45) * 4)) : 0;
  const fuel = prevFrame ? Math.max(0, prevFrame.fuelLevelLbs - (2.5 + Math.random())) : 18500;
  const rpm = prevFrame ? Math.min(9800, Math.max(800, prevFrame.engineRpm + (Math.random() - 0.5) * 150)) : 1200;

  return {
    timestamp: new Date().toISOString(),
    flightId: 'FL-2026-0042',
    aircraftId: 'N737SV',
    flightPhase: phase,
    altitudeFt: parseFloat(alt.toFixed(2)),
    airspeedKts: parseFloat(spd.toFixed(2)),
    headingDeg: parseFloat(((prevFrame?.headingDeg || 130) + (Math.random() - 0.5) * 0.3).toFixed(2)),
    verticalSpeedFpm: parseFloat(((Math.random() - 0.48) * 800).toFixed(2)),
    fuelLevelLbs: parseFloat(fuel.toFixed(2)),
    engineRpm: parseFloat(rpm.toFixed(2)),
    engineTempC: parseFloat((580 + Math.random() * 300).toFixed(2)),
    oatC: parseFloat((-51 + (Math.random() - 0.5) * 3).toFixed(2)),
    cabinPressurePsi: parseFloat((11.2 + (Math.random() - 0.5) * 0.2).toFixed(2)),
    batteryVolts: parseFloat((28.0 + Math.random() * 0.4).toFixed(2)),
    landingGearStatus: alt < 500 ? 'EXTENDED' : 'RETRACTED',
    flapsDegrees: phase === 'LANDING' ? 30 : phase === 'TAKEOFF' ? 15 : 0,
    autopilotEngaged: phase === 'CRUISE' || phase === 'CLIMB',
  };
};

const GaugeCard = ({ label, value, unit, color = '#38bdf8', mono = true }) => (
  <div className="glass-panel gauge-card">
    <div className="gauge-label">{label}</div>
    <div>
      <span className="gauge-value" style={{ color, fontFamily: mono ? 'var(--font-mono)' : 'var(--font-sans)' }}>
        {value ?? '—'}
      </span>
      {unit && <span className="gauge-unit">{unit}</span>}
    </div>
  </div>
);

const LiveTelemetry = () => {
  const [frame, setFrame] = useState(null);
  const [elapsed, setElapsed] = useState(0);
  const [isLive, setIsLive] = useState(true);
  const [history, setHistory] = useState([]);
  const intervalRef = useRef(null);

  const tick = () => {
    setElapsed(prev => {
      const nextElapsed = prev + 1;
      setFrame(prevFrame => {
        const newFrame = generateMockFrame(prevFrame, nextElapsed);
        setHistory(h => [newFrame, ...h].slice(0, 20));
        return newFrame;
      });
      return nextElapsed;
    });
  };

  useEffect(() => {
    if (isLive) {
      intervalRef.current = setInterval(tick, 1000);
    } else {
      clearInterval(intervalRef.current);
    }
    return () => clearInterval(intervalRef.current);
  }, [isLive]);

  if (!frame) return <LoadingSpinner label="Connecting to flight recorder stream..." />;

  const phaseColor = { PRE_FLIGHT: '#94a3b8', TAKEOFF: '#f59e0b', CLIMB: '#38bdf8', CRUISE: '#10b981', DESCENT: '#f59e0b', LANDING: '#f43f5e' };
  const currentPhaseColor = phaseColor[frame.flightPhase] || '#38bdf8';

  return (
    <div className="page-container">
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h1 className="page-title">Live Telemetry Cockpit</h1>
          <p className="page-subtitle">Real-time flight data recorder stream — updated every second.</p>
        </div>
        <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
          <div style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#64748b' }}>
            Elapsed: <span style={{ color: '#38bdf8' }}>{String(Math.floor(elapsed / 60)).padStart(2, '0')}:{String(elapsed % 60).padStart(2, '0')}</span>
          </div>
          <button
            onClick={() => setIsLive(l => !l)}
            style={{
              display: 'flex', alignItems: 'center', gap: '0.5rem',
              padding: '0.5rem 1rem',
              background: isLive ? 'rgba(244, 63, 94, 0.1)' : 'rgba(16, 185, 129, 0.1)',
              color: isLive ? '#f43f5e' : '#10b981',
              border: `1px solid ${isLive ? 'rgba(244,63,94,0.3)' : 'rgba(16,185,129,0.3)'}`,
              borderRadius: '8px', fontSize: '0.85rem', fontWeight: 600, cursor: 'pointer'
            }}
          >
            {isLive ? <><Wifi size={15} /> Pause Stream</> : <><RefreshCw size={15} /> Resume Stream</>}
          </button>
        </div>
      </div>

      {/* Flight & Phase Identity Bar */}
      <div className="glass-panel" style={{ padding: '1rem 1.5rem', marginBottom: '1.5rem', display: 'flex', gap: '2rem', flexWrap: 'wrap', alignItems: 'center' }}>
        {[
          { label: 'Flight ID', value: frame.flightId, mono: true, color: '#38bdf8' },
          { label: 'Aircraft', value: frame.aircraftId, mono: true, color: '#f8fafc' },
          { label: 'Gear', value: frame.landingGearStatus, mono: false, color: frame.landingGearStatus === 'EXTENDED' ? '#f43f5e' : '#10b981' },
          { label: 'Autopilot', value: frame.autopilotEngaged ? 'ENGAGED' : 'OFF', mono: false, color: frame.autopilotEngaged ? '#10b981' : '#64748b' },
          { label: 'Flaps', value: `${frame.flapsDegrees}°`, mono: true, color: '#f59e0b' },
        ].map((item, i) => (
          <div key={i}>
            <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.07em', color: '#64748b', marginBottom: '0.2rem' }}>{item.label}</p>
            <p style={{ fontFamily: item.mono ? 'var(--font-mono)' : 'var(--font-sans)', fontWeight: 700, fontSize: '1rem', color: item.color }}>{item.value}</p>
          </div>
        ))}
        <div style={{ marginLeft: 'auto' }}>
          <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.07em', color: '#64748b', marginBottom: '0.25rem' }}>Flight Phase</p>
          <span className="badge" style={{ background: `${currentPhaseColor}20`, color: currentPhaseColor, border: `1px solid ${currentPhaseColor}40`, fontSize: '0.85rem', padding: '0.35rem 1rem' }}>
            {isLive && <span className="live-indicator"></span>}
            {frame.flightPhase.replace('_', ' ')}
          </span>
        </div>
      </div>

      {/* Primary Telemetry Gauges */}
      <div className="telemetry-grid" style={{ marginBottom: '1.5rem' }}>
        <GaugeCard label="Altitude" value={formatNumber(frame.altitudeFt, 0)} unit="ft" color="#38bdf8" />
        <GaugeCard label="Airspeed" value={formatNumber(frame.airspeedKts, 1)} unit="kts" color="#10b981" />
        <GaugeCard label="Heading" value={formatNumber(frame.headingDeg, 1)} unit="°" color="#a855f7" />
        <GaugeCard label="Vertical Speed" value={formatNumber(frame.verticalSpeedFpm, 0)} unit="fpm" color={frame.verticalSpeedFpm >= 0 ? '#10b981' : '#f43f5e'} />
        <GaugeCard label="Fuel Level" value={formatNumber(frame.fuelLevelLbs, 0)} unit="lbs" color="#f59e0b" />
        <GaugeCard label="Engine RPM" value={formatNumber(frame.engineRpm, 0)} unit="rpm" color="#38bdf8" />
        <GaugeCard label="Engine Temp" value={formatNumber(frame.engineTempC, 1)} unit="°C" color={frame.engineTempC > 900 ? '#f43f5e' : '#f59e0b'} />
        <GaugeCard label="Cabin Pressure" value={formatNumber(frame.cabinPressurePsi, 2)} unit="PSI" color="#94a3b8" />
        <GaugeCard label="OAT" value={formatNumber(frame.oatC, 1)} unit="°C" color="#64748b" />
        <GaugeCard label="Battery Bus" value={formatNumber(frame.batteryVolts, 2)} unit="V" color="#10b981" />
      </div>

      {/* Telemetry History Log */}
      <div className="glass-panel" style={{ padding: '1.25rem' }}>
        <h3 style={{ fontSize: '1rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Activity size={18} color="#38bdf8" /> Recent Frame History
        </h3>
        <div style={{ overflowX: 'auto' }}>
          <table className="custom-table">
            <thead>
              <tr>
                <th>Time</th>
                <th>Phase</th>
                <th>Alt (ft)</th>
                <th>Speed (kts)</th>
                <th>Hdg (°)</th>
                <th>Fuel (lbs)</th>
                <th>RPM</th>
                <th>EGT (°C)</th>
              </tr>
            </thead>
            <tbody>
              {history.map((h, i) => (
                <tr key={i} style={{ opacity: 1 - i * 0.04 }}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: '#38bdf8' }}>{formatTimestamp(h.timestamp)}</td>
                  <td><Badge type="info">{h.flightPhase.replace('_', ' ')}</Badge></td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{formatNumber(h.altitudeFt, 0)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{formatNumber(h.airspeedKts, 1)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{formatNumber(h.headingDeg, 1)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{formatNumber(h.fuelLevelLbs, 0)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{formatNumber(h.engineRpm, 0)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{formatNumber(h.engineTempC, 1)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default LiveTelemetry;
