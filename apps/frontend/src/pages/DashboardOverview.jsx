import React from 'react';
import { Plane, Radio, CheckCircle, ShieldAlert, LinkCheck, Clock } from 'lucide-react';
import StatCard from '../components/common/StatCard';
import Badge from '../components/common/Badge';

const DashboardOverview = () => {
  const stats = [
    { title: 'Total Fleet Aircraft', value: '18', icon: Plane, trend: '+2', color: '#38bdf8' },
    { title: 'Active Live Flights', value: '4', icon: Radio, trend: '+1', color: '#10b981' },
    { title: 'Completed Sessions', value: '1,420', icon: CheckCircle, trend: '+34', color: '#3b82f6' },
    { title: 'AI Anomaly Alerts', value: '2', icon: ShieldAlert, trend: '-1', color: '#f43f5e' },
    { title: 'Blockchain Integrity', value: '100% Synced', icon: LinkCheck, trend: 'VERIFIED', color: '#a855f7' },
  ];

  const recentFlights = [
    { code: 'FL-2026-0042', tail: 'N737SV', origin: 'KJFK', dest: 'EGLL', phase: 'CRUISE', alt: '33,000 ft', status: 'IN_FLIGHT' },
    { code: 'FL-2026-0041', tail: 'N320SV', origin: 'KLAX', dest: 'KSFO', phase: 'LANDING', alt: '1,200 ft', status: 'IN_FLIGHT' },
    { code: 'FL-2026-0040', tail: 'N787SV', origin: 'EDDF', dest: 'OMDB', phase: 'CLIMB', alt: '18,500 ft', status: 'IN_FLIGHT' },
    { code: 'FL-2026-0039', tail: 'N737SV', origin: 'KORD', dest: 'KMIA', phase: 'PRE_FLIGHT', alt: '14 ft', status: 'IN_FLIGHT' },
    { code: 'FL-2026-0038', tail: 'N350SV', origin: 'RJTT', dest: 'VHHH', phase: 'COMPLETED', alt: '0 ft', status: 'COMPLETED' },
  ];

  return (
    <div className="page-container">
      <h1 className="page-title">Cloud Recorder Overview</h1>
      <p className="page-subtitle">Real-time status of connected flight recorders, AI anomalies & blockchain verification state.</p>

      {/* KPI Stats Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1.25rem', marginBottom: '2rem' }}>
        {stats.map((stat, idx) => (
          <StatCard key={idx} {...stat} />
        ))}
      </div>

      {/* Recent Flight Activity Table */}
      <div className="glass-panel" style={{ padding: '1.5rem' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.25rem' }}>
          <div>
            <h3 style={{ fontSize: '1.1rem' }}>Active & Recent Flight Streams</h3>
            <p style={{ fontSize: '0.8rem', color: '#64748b' }}>Live black box data ingestion sessions</p>
          </div>
          <button style={{
            background: 'rgba(56, 189, 248, 0.1)',
            color: '#38bdf8',
            border: '1px solid rgba(56, 189, 248, 0.3)',
            padding: '0.4rem 0.875rem',
            borderRadius: '6px',
            fontSize: '0.8rem',
            cursor: 'pointer'
          }}>
            View All Streams
          </button>
        </div>

        <table className="custom-table">
          <thead>
            <tr>
              <th>Flight Session</th>
              <th>Aircraft Tail</th>
              <th>Route (Origin ➔ Dest)</th>
              <th>Current Phase</th>
              <th>Altitude</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {recentFlights.map((flight) => (
              <tr key={flight.code}>
                <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 600, color: '#f8fafc' }}>{flight.code}</td>
                <td>{flight.tail}</td>
                <td>{flight.origin} ➔ {flight.dest}</td>
                <td><Badge type="info">{flight.phase}</Badge></td>
                <td style={{ fontFamily: 'var(--font-mono)' }}>{flight.alt}</td>
                <td>
                  <Badge type={flight.status === 'IN_FLIGHT' ? 'active' : 'info'}>
                    {flight.status}
                  </Badge>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default DashboardOverview;
