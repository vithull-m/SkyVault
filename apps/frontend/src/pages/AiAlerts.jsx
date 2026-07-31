import React, { useState } from 'react';
import Badge from '../components/common/Badge';
import EmptyState from '../components/common/EmptyState';
import { ShieldAlert, Filter } from 'lucide-react';
import { formatTimestamp } from '../utils/formatters';

const MOCK_ALERTS = [
  {
    id: 'DET-001',
    flightId: 'FL-2026-0042',
    aircraft: 'N737SV',
    anomalyType: 'RAPID_ALTITUDE_LOSS',
    severityScore: 0.87,
    confidenceScore: 0.94,
    startTimestamp: '2026-07-31T09:12:14Z',
    endTimestamp: '2026-07-31T09:12:46Z',
    modelVersion: 'skyvault-lstm-v2.1',
    description: 'Unscheduled altitude loss of 1,800 ft in 32 seconds during cruise phase detected.',
    status: 'UNREVIEWED',
  },
  {
    id: 'DET-002',
    flightId: 'FL-2026-0041',
    aircraft: 'N320SV',
    anomalyType: 'ENGINE_TEMP_SPIKE',
    severityScore: 0.62,
    confidenceScore: 0.88,
    startTimestamp: '2026-07-31T10:45:02Z',
    endTimestamp: '2026-07-31T10:45:28Z',
    modelVersion: 'skyvault-lstm-v2.1',
    description: 'Engine exhaust gas temperature exceeded 980°C for 26 seconds on Engine 1 during climb.',
    status: 'UNDER_REVIEW',
  },
  {
    id: 'DET-003',
    flightId: 'FL-2026-0038',
    aircraft: 'N350SV',
    anomalyType: 'STALL_RISK',
    severityScore: 0.34,
    confidenceScore: 0.76,
    startTimestamp: '2026-07-31T04:12:00Z',
    endTimestamp: '2026-07-31T04:12:11Z',
    modelVersion: 'skyvault-xgb-v1.4',
    description: 'Airspeed approached Vs1 threshold during final approach configuration change.',
    status: 'CLEARED',
  },
];

const severityBadge = (score) => {
  if (score >= 0.75) return <Badge type="high">HIGH {(score * 100).toFixed(0)}%</Badge>;
  if (score >= 0.50) return <Badge type="medium">MEDIUM {(score * 100).toFixed(0)}%</Badge>;
  return <Badge type="low">LOW {(score * 100).toFixed(0)}%</Badge>;
};

const statusBadge = (status) => {
  if (status === 'UNREVIEWED') return <Badge type="danger">UNREVIEWED</Badge>;
  if (status === 'UNDER_REVIEW') return <Badge type="warning">UNDER REVIEW</Badge>;
  return <Badge type="active">CLEARED</Badge>;
};

const AiAlerts = () => {
  const [filter, setFilter] = useState('ALL');
  const filters = ['ALL', 'UNREVIEWED', 'UNDER_REVIEW', 'CLEARED'];

  const filtered = MOCK_ALERTS.filter(a => filter === 'ALL' || a.status === filter);

  return (
    <div className="page-container">
      <h1 className="page-title">AI Anomaly Detection Alerts</h1>
      <p className="page-subtitle">Machine learning detected flight anomalies requiring safety review.</p>

      {/* KPI Strip */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginBottom: '1.5rem' }}>
        {[
          { label: 'Unreviewed', value: MOCK_ALERTS.filter(a => a.status === 'UNREVIEWED').length, color: '#f43f5e' },
          { label: 'Under Review', value: MOCK_ALERTS.filter(a => a.status === 'UNDER_REVIEW').length, color: '#f59e0b' },
          { label: 'Cleared', value: MOCK_ALERTS.filter(a => a.status === 'CLEARED').length, color: '#10b981' },
        ].map((s, i) => (
          <div key={i} className="glass-panel" style={{ padding: '1rem 1.25rem' }}>
            <p style={{ fontSize: '0.75rem', color: '#64748b', textTransform: 'uppercase' }}>{s.label}</p>
            <h3 style={{ fontSize: '1.75rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: s.color }}>{s.value}</h3>
          </div>
        ))}
      </div>

      {/* Filter Tabs */}
      <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '1.25rem' }}>
        {filters.map(f => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            style={{
              padding: '0.4rem 1rem',
              borderRadius: '6px',
              border: '1px solid rgba(255,255,255,0.1)',
              background: filter === f ? 'rgba(56,189,248,0.15)' : 'transparent',
              color: filter === f ? '#38bdf8' : '#94a3b8',
              fontWeight: filter === f ? 600 : 400,
              fontSize: '0.8rem',
              cursor: 'pointer',
              transition: 'all 0.2s',
            }}
          >
            {f.replace('_', ' ')}
          </button>
        ))}
      </div>

      {/* Alerts List */}
      {filtered.length === 0 ? (
        <EmptyState title="No Alerts Found" description="No anomalies match the current filter. All flights are operating normally." />
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {filtered.map(alert => (
            <div key={alert.id} className="glass-panel" style={{ padding: '1.25rem 1.5rem', borderLeft: `4px solid ${alert.severityScore >= 0.75 ? '#f43f5e' : alert.severityScore >= 0.5 ? '#f59e0b' : '#10b981'}` }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.875rem', flexWrap: 'wrap', gap: '0.5rem' }}>
                <div style={{ display: 'flex', gap: '1rem', alignItems: 'center', flexWrap: 'wrap' }}>
                  <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, color: '#f8fafc', fontSize: '1rem' }}>{alert.id}</span>
                  <span style={{ background: 'rgba(56,189,248,0.1)', color: '#38bdf8', padding: '0.2rem 0.6rem', borderRadius: '4px', fontSize: '0.78rem', fontFamily: 'var(--font-mono)' }}>
                    {alert.anomalyType.replace(/_/g, ' ')}
                  </span>
                  {severityBadge(alert.severityScore)}
                  {statusBadge(alert.status)}
                </div>
                <button style={{ padding: '0.35rem 0.875rem', background: 'rgba(56,189,248,0.1)', color: '#38bdf8', border: '1px solid rgba(56,189,248,0.3)', borderRadius: '6px', fontSize: '0.8rem', cursor: 'pointer', fontWeight: 500 }}>
                  Review
                </button>
              </div>

              <p style={{ color: '#94a3b8', fontSize: '0.88rem', marginBottom: '1rem', lineHeight: 1.6 }}>{alert.description}</p>

              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '0.75rem' }}>
                {[
                  { k: 'Flight', v: alert.flightId },
                  { k: 'Aircraft', v: alert.aircraft },
                  { k: 'Start', v: formatTimestamp(alert.startTimestamp) },
                  { k: 'End', v: formatTimestamp(alert.endTimestamp) },
                  { k: 'Confidence', v: `${(alert.confidenceScore * 100).toFixed(0)}%` },
                  { k: 'Model', v: alert.modelVersion },
                ].map((item, i) => (
                  <div key={i}>
                    <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: '#64748b' }}>{item.k}</p>
                    <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.82rem', color: '#f8fafc' }}>{item.v}</p>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AiAlerts;
