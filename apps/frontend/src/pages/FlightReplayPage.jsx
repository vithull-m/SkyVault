import React, { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Badge from '../components/common/Badge';
import { Play, Pause, RotateCcw, ShieldAlert, ShieldCheck, Activity, FastForward } from 'lucide-react';
import { formatNumber } from '../utils/formatters';

const MOCK_REPLAY_FRAMES = [
  { index: 0, time: '00:00:00', phase: 'PRE_FLIGHT', alt: 12, speed: 0, verticalSpeed: 0, fuel: 18500, rpm: 1200, gear: 'EXTENDED', flaps: 15, alert: null },
  { index: 1, time: '00:00:15', phase: 'TAKEOFF', alt: 45, speed: 145, verticalSpeed: 1400, fuel: 18450, rpm: 9600, gear: 'EXTENDED', flaps: 15, alert: null },
  { index: 2, time: '00:01:00', phase: 'CLIMB', alt: 8500, speed: 250, verticalSpeed: 2400, fuel: 18200, rpm: 8900, gear: 'RETRACTED', flaps: 0, alert: null },
  { index: 3, time: '00:02:15', phase: 'CLIMB', alt: 18500, speed: 320, verticalSpeed: 2200, fuel: 17800, rpm: 8900, gear: 'RETRACTED', flaps: 0, alert: null },
  { index: 4, time: '00:03:30', phase: 'CRUISE', alt: 33000, speed: 450, verticalSpeed: 0, fuel: 17200, rpm: 7400, gear: 'RETRACTED', flaps: 0, alert: null },
  { index: 5, time: '00:04:10', phase: 'CRUISE', alt: 31200, speed: 440, verticalSpeed: -3800, fuel: 16900, rpm: 7200, gear: 'RETRACTED', flaps: 0, alert: 'RAPID_ALTITUDE_DROP' },
  { index: 6, time: '00:05:00', phase: 'DESCENT', alt: 15000, speed: 280, verticalSpeed: -1800, fuel: 16500, rpm: 4200, gear: 'RETRACTED', flaps: 0, alert: null },
  { index: 7, time: '00:06:15', phase: 'LANDING', alt: 450, speed: 140, verticalSpeed: -600, fuel: 16100, rpm: 2200, gear: 'EXTENDED', flaps: 30, alert: null },
  { index: 8, time: '00:07:00', phase: 'LANDING', alt: 12, speed: 0, verticalSpeed: 0, fuel: 16000, rpm: 1000, gear: 'EXTENDED', flaps: 30, alert: null },
];

const FlightReplayPage = () => {
  const { flightId } = useParams();
  const navigate = useNavigate();

  const [currentIndex, setCurrentIndex] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [playbackSpeed, setPlaybackSpeed] = useState(1);
  const timerRef = useRef(null);

  const currentFrame = MOCK_REPLAY_FRAMES[currentIndex];

  useEffect(() => {
    if (isPlaying) {
      timerRef.current = setInterval(() => {
        setCurrentIndex(prev => {
          if (prev >= MOCK_REPLAY_FRAMES.length - 1) {
            setIsPlaying(false);
            return prev;
          }
          return prev + 1;
        });
      }, 1500 / playbackSpeed);
    } else {
      clearInterval(timerRef.current);
    }
    return () => clearInterval(timerRef.current);
  }, [isPlaying, playbackSpeed]);

  const handleSeek = (e) => {
    setCurrentIndex(Number(e.target.value));
  };

  return (
    <div className="page-container">
      {/* Header Bar */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <button onClick={() => navigate(`/investigation/detail/${flightId || 'FL-2026-0042'}`)} style={{ background: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', padding: '0.4rem 0.875rem', borderRadius: '6px', cursor: 'pointer', fontSize: '0.8rem', marginBottom: '0.75rem' }}>
            ← Back to Case File
          </button>
          <h1 className="page-title">Chronological Telemetry Replay</h1>
          <p className="page-subtitle">Interactive Flight Simulator Replay — {flightId || 'FL-2026-0042'}</p>
        </div>

        <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
          <Badge type="active"><ShieldCheck size={14} style={{ marginRight: '4px' }} /> BLOCKCHAIN VERIFIED</Badge>
        </div>
      </div>

      {/* Replay Control Scrubbing Bar */}
      <div className="glass-panel" style={{ padding: '1.25rem 1.5rem', marginBottom: '1.5rem' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1.25rem', marginBottom: '1rem', flexWrap: 'wrap' }}>
          <button
            onClick={() => setIsPlaying(!isPlaying)}
            style={{ padding: '0.6rem 1.25rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 600, fontSize: '0.85rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.5rem' }}
          >
            {isPlaying ? <><Pause size={16} /> Pause</> : <><Play size={16} /> Play</>}
          </button>

          <button
            onClick={() => { setIsPlaying(false); setCurrentIndex(0); }}
            style={{ padding: '0.6rem', background: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', cursor: 'pointer' }}
          >
            <RotateCcw size={16} />
          </button>

          {/* Playback speed selector */}
          <div style={{ display: 'flex', gap: '0.25rem' }}>
            {[1, 2, 4].map(spd => (
              <button
                key={spd}
                onClick={() => setPlaybackSpeed(spd)}
                style={{
                  padding: '0.35rem 0.65rem', borderRadius: '4px', border: 'none', cursor: 'pointer', fontSize: '0.75rem', fontWeight: 600,
                  background: playbackSpeed === spd ? 'rgba(56,189,248,0.2)' : 'rgba(255,255,255,0.05)',
                  color: playbackSpeed === spd ? '#38bdf8' : '#64748b'
                }}
              >
                {spd}x
              </button>
            ))}
          </div>

          <div style={{ marginLeft: 'auto', fontFamily: 'var(--font-mono)', fontSize: '0.9rem', color: '#38bdf8' }}>
            Frame {currentIndex + 1} / {MOCK_REPLAY_FRAMES.length} [{currentFrame.time}]
          </div>
        </div>

        {/* Timeline Slider */}
        <input
          type="range"
          min="0"
          max={MOCK_REPLAY_FRAMES.length - 1}
          value={currentIndex}
          onChange={handleSeek}
          style={{ width: '100%', cursor: 'pointer', accentColor: '#38bdf8' }}
        />
      </div>

      {/* Triggered Alert Notification Banner */}
      {currentFrame.alert && (
        <div className="glass-panel" style={{ padding: '1rem 1.25rem', marginBottom: '1.5rem', borderLeft: '4px solid #f43f5e', background: 'rgba(244,63,94,0.1)', display: 'flex', alignItems: 'center', gap: '0.875rem' }}>
          <ShieldAlert size={24} color="#f43f5e" />
          <div>
            <h4 style={{ color: '#f43f5e', fontSize: '0.95rem' }}>AI ANOMALY DETECTED: {currentFrame.alert}</h4>
            <p style={{ color: '#cbd5e1', fontSize: '0.82rem' }}>Vertical speed drop of {currentFrame.verticalSpeed} fpm triggered an anomaly alert frame during flight replay.</p>
          </div>
        </div>
      )}

      {/* Flight Instrument Gauges */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem' }}>
        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Flight Phase</p>
          <h3 style={{ fontSize: '1.4rem', color: '#38bdf8', marginTop: '0.25rem' }}>{currentFrame.phase}</h3>
        </div>

        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Altitude</p>
          <h3 style={{ fontSize: '1.6rem', fontFamily: 'var(--font-mono)', color: '#10b981', marginTop: '0.25rem' }}>{formatNumber(currentFrame.alt, 0)} <span style={{ fontSize: '0.85rem' }}>ft</span></h3>
        </div>

        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Airspeed</p>
          <h3 style={{ fontSize: '1.6rem', fontFamily: 'var(--font-mono)', color: '#38bdf8', marginTop: '0.25rem' }}>{formatNumber(currentFrame.speed, 0)} <span style={{ fontSize: '0.85rem' }}>kts</span></h3>
        </div>

        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Vertical Speed</p>
          <h3 style={{ fontSize: '1.6rem', fontFamily: 'var(--font-mono)', color: currentFrame.verticalSpeed < 0 ? '#f43f5e' : '#10b981', marginTop: '0.25rem' }}>{currentFrame.verticalSpeed} <span style={{ fontSize: '0.85rem' }}>fpm</span></h3>
        </div>

        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Engine RPM</p>
          <h3 style={{ fontSize: '1.6rem', fontFamily: 'var(--font-mono)', color: '#a855f7', marginTop: '0.25rem' }}>{formatNumber(currentFrame.rpm, 0)}</h3>
        </div>

        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Gear / Flaps</p>
          <h3 style={{ fontSize: '1.1rem', color: '#f8fafc', marginTop: '0.25rem' }}>{currentFrame.gear} / {currentFrame.flaps}°</h3>
        </div>
      </div>
    </div>
  );
};

export default FlightReplayPage;
