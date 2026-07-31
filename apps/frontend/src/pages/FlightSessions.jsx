import React, { useState } from 'react';
import Badge from '../components/common/Badge';
import EmptyState from '../components/common/EmptyState';
import { Radio, Search } from 'lucide-react';

const MOCK_SESSIONS = [
  { code: 'FL-2026-0042', aircraftId: 'N737SV', departure: 'KJFK', arrival: 'EGLL', startTime: '2026-07-31T08:14:00Z', endTime: null, status: 'IN_FLIGHT', phase: 'CRUISE', frames: 18432 },
  { code: 'FL-2026-0041', aircraftId: 'N320SV', departure: 'KLAX', arrival: 'KSFO', startTime: '2026-07-31T09:55:00Z', endTime: null, status: 'IN_FLIGHT', phase: 'LANDING', frames: 6120 },
  { code: 'FL-2026-0040', aircraftId: 'N787SV', departure: 'EDDF', arrival: 'OMDB', startTime: '2026-07-31T06:30:00Z', endTime: null, status: 'IN_FLIGHT', phase: 'CLIMB', frames: 26780 },
  { code: 'FL-2026-0039', aircraftId: 'N737SV', departure: 'KORD', arrival: 'KMIA', startTime: '2026-07-31T10:02:00Z', endTime: null, status: 'IN_FLIGHT', phase: 'PRE_FLIGHT', frames: 950 },
  { code: 'FL-2026-0038', aircraftId: 'N350SV', departure: 'RJTT', arrival: 'VHHH', startTime: '2026-07-31T01:15:00Z', endTime: '2026-07-31T05:44:00Z', status: 'COMPLETED', phase: 'LANDING', frames: 16141 },
  { code: 'FL-2026-0037', aircraftId: 'N172SV', departure: 'KPAO', arrival: 'KSJC', startTime: '2026-07-30T14:00:00Z', endTime: '2026-07-30T14:32:00Z', status: 'COMPLETED', phase: 'LANDING', frames: 1943 },
];

const FlightSessions = () => {
  const [search, setSearch] = useState('');

  const filtered = MOCK_SESSIONS.filter(s =>
    s.code.toLowerCase().includes(search.toLowerCase()) ||
    s.aircraftId.toLowerCase().includes(search.toLowerCase()) ||
    s.departure.toLowerCase().includes(search.toLowerCase()) ||
    s.arrival.toLowerCase().includes(search.toLowerCase())
  );

  const phaseBadge = (phase) => {
    const phaseMap = {
      PRE_FLIGHT: 'info',
      TAKEOFF: 'warning',
      CLIMB: 'warning',
      CRUISE: 'active',
      DESCENT: 'warning',
      LANDING: 'info',
      COMPLETED: 'active',
    };
    return <Badge type={phaseMap[phase] || 'info'}>{phase.replace('_', ' ')}</Badge>;
  };

  const statusBadge = (status) => {
    if (status === 'IN_FLIGHT') return <Badge type="active">IN FLIGHT</Badge>;
    if (status === 'COMPLETED') return <Badge type="info">COMPLETED</Badge>;
    return <Badge type="danger">ABORTED</Badge>;
  };

  const formatTime = (iso) => {
    if (!iso) return '—';
    return new Date(iso).toLocaleString('en-US', { month: 'short', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: false });
  };

  return (
    <div className="page-container">
      <h1 className="page-title">Flight Recording Sessions</h1>
      <p className="page-subtitle">Browse all active and archived black box data ingestion sessions.</p>

      {/* Stats Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginBottom: '1.5rem' }}>
        {[
          { label: 'Active Streams', value: MOCK_SESSIONS.filter(s => s.status === 'IN_FLIGHT').length, color: '#10b981' },
          { label: 'Completed', value: MOCK_SESSIONS.filter(s => s.status === 'COMPLETED').length, color: '#38bdf8' },
          { label: 'Total Telemetry Frames', value: MOCK_SESSIONS.reduce((sum, s) => sum + s.frames, 0).toLocaleString(), color: '#a855f7' },
        ].map((s, i) => (
          <div key={i} className="glass-panel" style={{ padding: '1rem 1.25rem' }}>
            <p style={{ fontSize: '0.75rem', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.05em' }}>{s.label}</p>
            <h3 style={{ fontSize: '1.6rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: s.color }}>{s.value}</h3>
          </div>
        ))}
      </div>

      {/* Search */}
      <div style={{ position: 'relative', marginBottom: '1.25rem' }}>
        <Search size={15} color="#64748b" style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)' }} />
        <input
          type="text"
          placeholder="Search by flight code, aircraft, or route..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          style={{ width: '100%', padding: '0.65rem 1rem 0.65rem 2.25rem', borderRadius: '8px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', fontSize: '0.85rem', outline: 'none' }}
        />
      </div>

      {/* Table */}
      <div className="glass-panel" style={{ overflow: 'hidden' }}>
        {filtered.length === 0 ? (
          <EmptyState title="No Sessions Found" description="Try adjusting your search query." />
        ) : (
          <table className="custom-table">
            <thead>
              <tr>
                <th>Session Code</th>
                <th>Aircraft</th>
                <th>Route</th>
                <th>Start Time</th>
                <th>End Time</th>
                <th>Phase</th>
                <th>Telemetry Frames</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(s => (
                <tr key={s.code}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, color: '#38bdf8' }}>
                    {s.status === 'IN_FLIGHT' && <span className="live-indicator"></span>}
                    {s.code}
                  </td>
                  <td style={{ color: '#f8fafc', fontWeight: 500 }}>{s.aircraftId}</td>
                  <td>{s.departure} → {s.arrival}</td>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem' }}>{formatTime(s.startTime)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem' }}>{formatTime(s.endTime)}</td>
                  <td>{phaseBadge(s.phase)}</td>
                  <td style={{ fontFamily: 'var(--font-mono)', color: '#a855f7' }}>{s.frames.toLocaleString()}</td>
                  <td>{statusBadge(s.status)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default FlightSessions;
