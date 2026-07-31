import React, { useState } from 'react';
import Badge from '../components/common/Badge';
import EmptyState from '../components/common/EmptyState';
import { Plane, Plus, Search, Filter } from 'lucide-react';

const MOCK_AIRCRAFT = [
  { id: '1', registrationNumber: 'N737SV', model: 'Boeing 737-800', manufacturer: 'Boeing', airlineName: 'SkyVault Airways', manufacturingYear: 2018, capacity: 189, engineType: 'CFM56-7B', status: 'ACTIVE' },
  { id: '2', registrationNumber: 'N320SV', model: 'Airbus A320neo', manufacturer: 'Airbus', airlineName: 'SkyVault Airways', manufacturingYear: 2020, capacity: 165, engineType: 'CFM LEAP-1A', status: 'ACTIVE' },
  { id: '3', registrationNumber: 'N787SV', model: 'Boeing 787-9', manufacturer: 'Boeing', airlineName: 'SkyVault Airways', manufacturingYear: 2021, capacity: 296, engineType: 'GEnx-1B', status: 'MAINTENANCE' },
  { id: '4', registrationNumber: 'N350SV', model: 'Airbus A350-900', manufacturer: 'Airbus', airlineName: 'SkyVault Airways', manufacturingYear: 2019, capacity: 369, engineType: 'Trent XWB-84', status: 'ACTIVE' },
  { id: '5', registrationNumber: 'N172SV', model: 'Cessna 172 Skyhawk', manufacturer: 'Cessna', airlineName: 'SkyVault Training', manufacturingYear: 2016, capacity: 4, engineType: 'Lycoming IO-360', status: 'RETIRED' },
];

const AircraftFleet = () => {
  const [search, setSearch] = useState('');

  const filtered = MOCK_AIRCRAFT.filter(a =>
    a.registrationNumber.toLowerCase().includes(search.toLowerCase()) ||
    a.model.toLowerCase().includes(search.toLowerCase()) ||
    a.airlineName.toLowerCase().includes(search.toLowerCase())
  );

  const statusBadge = (status) => {
    if (status === 'ACTIVE') return <Badge type="active">ACTIVE</Badge>;
    if (status === 'MAINTENANCE') return <Badge type="warning">MAINTENANCE</Badge>;
    return <Badge type="danger">RETIRED</Badge>;
  };

  return (
    <div className="page-container">
      <h1 className="page-title">Aircraft Fleet Registry</h1>
      <p className="page-subtitle">Manage and monitor the registered aircraft fleet connected to the SkyVault recorder network.</p>

      {/* Toolbar */}
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem', flexWrap: 'wrap' }}>
        <div style={{ position: 'relative', flex: 1, minWidth: '200px' }}>
          <Search size={15} color="#64748b" style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)' }} />
          <input
            type="text"
            placeholder="Search by tail number, model, or airline..."
            value={search}
            onChange={e => setSearch(e.target.value)}
            style={{ width: '100%', padding: '0.6rem 1rem 0.6rem 2.25rem', borderRadius: '8px', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', fontSize: '0.85rem', outline: 'none' }}
          />
        </div>
        <button style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.6rem 1.25rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 600, fontSize: '0.875rem', cursor: 'pointer' }}>
          <Plus size={16} /> Register Aircraft
        </button>
      </div>

      {/* Stats Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginBottom: '1.5rem' }}>
        {[
          { label: 'Total Registered', value: MOCK_AIRCRAFT.length, color: '#38bdf8' },
          { label: 'Active', value: MOCK_AIRCRAFT.filter(a => a.status === 'ACTIVE').length, color: '#10b981' },
          { label: 'Under Maintenance', value: MOCK_AIRCRAFT.filter(a => a.status === 'MAINTENANCE').length, color: '#f59e0b' },
        ].map((s, i) => (
          <div key={i} className="glass-panel" style={{ padding: '1rem 1.25rem' }}>
            <p style={{ fontSize: '0.75rem', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.05em' }}>{s.label}</p>
            <h3 style={{ fontSize: '1.75rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: s.color }}>{s.value}</h3>
          </div>
        ))}
      </div>

      {/* Table */}
      <div className="glass-panel" style={{ overflow: 'hidden' }}>
        {filtered.length === 0 ? (
          <EmptyState title="No Aircraft Found" description="No registered aircraft match your current search query." />
        ) : (
          <table className="custom-table">
            <thead>
              <tr>
                <th>Tail Number</th>
                <th>Model</th>
                <th>Manufacturer</th>
                <th>Airline</th>
                <th>Year</th>
                <th>Capacity</th>
                <th>Engine</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(a => (
                <tr key={a.id} style={{ cursor: 'pointer' }}>
                  <td style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, color: '#38bdf8' }}>{a.registrationNumber}</td>
                  <td style={{ color: '#f8fafc', fontWeight: 500 }}>{a.model}</td>
                  <td>{a.manufacturer}</td>
                  <td>{a.airlineName}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{a.manufacturingYear}</td>
                  <td style={{ fontFamily: 'var(--font-mono)' }}>{a.capacity} pax</td>
                  <td style={{ fontSize: '0.8rem' }}>{a.engineType}</td>
                  <td>{statusBadge(a.status)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default AircraftFleet;
