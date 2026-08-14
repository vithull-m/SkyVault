import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Badge from '../components/common/Badge';
import EmptyState from '../components/common/EmptyState';
import { FileSearch, Search, Play, FileText, Calendar, Filter } from 'lucide-react';

const MOCK_INCIDENTS = [
  { flightId: 'FL-2026-0042', tailNumber: 'N737SV', airline: 'SkyVault Airways', route: 'KJFK → EGLL', date: '2026-07-31', incidentType: 'RAPID_ALTITUDE_DROP', severity: 'HIGH', status: 'UNDER_INVESTIGATION', integrity: 'VERIFIED' },
  { flightId: 'FL-2026-0041', tailNumber: 'N320SV', airline: 'SkyVault Airways', route: 'KLAX → KSFO', date: '2026-07-31', incidentType: 'ENGINE_OVERHEATING', severity: 'MEDIUM', status: 'UNDER_INVESTIGATION', integrity: 'VERIFIED' },
  { flightId: 'FL-2026-0038', tailNumber: 'N350SV', airline: 'SkyVault Airways', route: 'RJTT → VHHH', date: '2026-07-31', incidentType: 'DATA_INTEGRITY_BREACH', severity: 'CRITICAL', status: 'TAMPER_ALERT', integrity: 'TAMPER_DETECTED' },
  { flightId: 'FL-2026-0035', tailNumber: 'N787SV', airline: 'SkyVault Airways', route: 'EGLL → KJFK', date: '2026-07-29', incidentType: 'CABIN_PRESSURE_LOSS', severity: 'HIGH', status: 'CLOSED', integrity: 'VERIFIED' },
];

const InvestigationDashboard = () => {
  const navigate = useNavigate();

  // Search Filters
  const [flightId, setFlightId] = useState('');
  const [aircraftId, setAircraftId] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [incidentType, setIncidentType] = useState('ALL');

  const filtered = MOCK_INCIDENTS.filter(item => {
    const matchFlight = !flightId || item.flightId.toLowerCase().includes(flightId.toLowerCase());
    const matchAircraft = !aircraftId || item.tailNumber.toLowerCase().includes(aircraftId.toLowerCase());
    const matchIncident = incidentType === 'ALL' || item.incidentType === incidentType;
    return matchFlight && matchAircraft && matchIncident;
  });

  return (
    <div className="page-container">
      <h1 className="page-title">Air Safety Investigation Portal</h1>
      <p className="page-subtitle">Government Investigation Agency — Forensic Flight Data & Evidence Hub.</p>

      {/* Search Filter Panel */}
      <div className="glass-panel" style={{ padding: '1.5rem', marginBottom: '1.5rem' }}>
        <h3 style={{ fontSize: '1rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Filter size={18} color="#38bdf8" /> Forensic Filter & Search Parameters
        </h3>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
          <div>
            <label style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b', display: 'block', marginBottom: '0.35rem' }}>Flight ID</label>
            <input
              type="text"
              placeholder="e.g. FL-2026-0042"
              value={flightId}
              onChange={e => setFlightId(e.target.value)}
              style={{ width: '100%', padding: '0.55rem 0.875rem', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '6px', fontSize: '0.85rem', outline: 'none' }}
            />
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b', display: 'block', marginBottom: '0.35rem' }}>Aircraft Tail Number</label>
            <input
              type="text"
              placeholder="e.g. N737SV"
              value={aircraftId}
              onChange={e => setAircraftId(e.target.value)}
              style={{ width: '100%', padding: '0.55rem 0.875rem', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '6px', fontSize: '0.85rem', outline: 'none' }}
            />
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b', display: 'block', marginBottom: '0.35rem' }}>Incident Classification</label>
            <select
              value={incidentType}
              onChange={e => setIncidentType(e.target.value)}
              style={{ width: '100%', padding: '0.55rem 0.875rem', background: '#1f2937', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '6px', fontSize: '0.85rem', outline: 'none', cursor: 'pointer' }}
            >
              <option value="ALL">All Incident Types</option>
              <option value="RAPID_ALTITUDE_DROP">Rapid Altitude Drop</option>
              <option value="ENGINE_OVERHEATING">Engine Overheating</option>
              <option value="DATA_INTEGRITY_BREACH">Data Integrity Breach</option>
              <option value="CABIN_PRESSURE_LOSS">Cabin Pressure Loss</option>
            </select>
          </div>

          <div>
            <label style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b', display: 'block', marginBottom: '0.35rem' }}>Start Date</label>
            <input
              type="date"
              value={startDate}
              onChange={e => setStartDate(e.target.value)}
              style={{ width: '100%', padding: '0.55rem 0.875rem', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '6px', fontSize: '0.85rem', outline: 'none' }}
            />
          </div>
        </div>
      </div>

      {/* Investigation Results Table */}
      <div className="glass-panel" style={{ overflow: 'hidden' }}>
        {filtered.length === 0 ? (
          <EmptyState title="No Incident Files Found" description="No investigation cases match your active filter parameters." />
        ) : (
          <table className="custom-table">
            <thead>
              <tr>
                <th>Flight ID</th>
                <th>Aircraft</th>
                <th>Route</th>
                <th>Date</th>
                <th>Incident Classification</th>
                <th>Integrity Check</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(item => (
                <tr key={item.flightId}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, color: '#38bdf8' }}>{item.flightId}</td>
                  <td>{item.tailNumber}</td>
                  <td>{item.route}</td>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: '0.82rem' }}>{item.date}</td>
                  <td><Badge type={item.severity === 'CRITICAL' ? 'danger' : 'warning'}>{item.incidentType.replace(/_/g, ' ')}</Badge></td>
                  <td>
                    <Badge type={item.integrity === 'VERIFIED' ? 'active' : 'danger'}>
                      {item.integrity === 'VERIFIED' ? 'VERIFIED' : '⚠ TAMPERED'}
                    </Badge>
                  </td>
                  <td>
                    <div style={{ display: 'flex', gap: '0.5rem' }}>
                      <button
                        onClick={() => navigate(`/investigation/detail/${item.flightId}`)}
                        style={{ padding: '0.35rem 0.75rem', background: 'rgba(56,189,248,0.1)', color: '#38bdf8', border: '1px solid rgba(56,189,248,0.3)', borderRadius: '6px', fontSize: '0.8rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.25rem' }}
                      >
                        <FileText size={14} /> Audit File
                      </button>
                      <button
                        onClick={() => navigate(`/investigation/replay/${item.flightId}`)}
                        style={{ padding: '0.35rem 0.75rem', background: 'rgba(16,185,129,0.1)', color: '#10b981', border: '1px solid rgba(16,185,129,0.3)', borderRadius: '6px', fontSize: '0.8rem', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.25rem' }}
                      >
                        <Play size={14} /> Replay Flight
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default InvestigationDashboard;
