import React, { useState } from 'react';
import Badge from '../components/common/Badge';
import EmptyState from '../components/common/EmptyState';
import { FileSearch, Plus, CheckCircle } from 'lucide-react';

const MOCK_REPORTS = [
  {
    id: 'INV-2026-004',
    title: 'Rapid Altitude Deviation – FL-2026-0042',
    sessionId: 'FL-2026-0042',
    investigator: 'Dr. Sarah Mitchell',
    summary: 'Investigation into unscheduled 1,800 ft altitude loss during cruise phase over the North Atlantic. AI anomaly score 0.87. Blockchain integrity confirmed: VERIFIED.',
    status: 'DRAFT',
    integrity: true,
    createdAt: '2026-07-31T11:00:00Z',
  },
  {
    id: 'INV-2026-003',
    title: 'Engine EGT Exceedance – FL-2026-0041',
    sessionId: 'FL-2026-0041',
    investigator: 'Cmdr. James Reeves',
    summary: 'Review of Engine 1 exhaust gas temperature breach during climb phase. Root cause identified as partial fuel nozzle clog. Cleared for return to service.',
    status: 'FINALIZED',
    integrity: true,
    createdAt: '2026-07-30T16:30:00Z',
  },
  {
    id: 'INV-2026-002',
    title: 'Data Integrity Breach – FL-2026-0038',
    sessionId: 'FL-2026-0038',
    investigator: 'Agent R. Torres',
    summary: 'Telemetry frame tampering detected by blockchain verification module for flight FL-2026-0038. Merkle root mismatch at block #12483720. Case referred to regulatory authority.',
    status: 'FINALIZED',
    integrity: false,
    createdAt: '2026-07-31T06:15:00Z',
  },
];

const InvestigationReports = () => {
  const [view, setView] = useState(null);
  const formatDate = (iso) => new Date(iso).toLocaleString('en-US', { month: 'short', day: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });

  if (view) {
    return (
      <div className="page-container">
        <button onClick={() => setView(null)} style={{ marginBottom: '1.5rem', background: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', padding: '0.5rem 1rem', borderRadius: '8px', cursor: 'pointer', fontSize: '0.85rem' }}>
          ← Back to Reports
        </button>
        <div className="glass-panel" style={{ padding: '2rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
            <div>
              <p style={{ fontFamily: 'var(--font-mono)', color: '#38bdf8', fontSize: '0.85rem', marginBottom: '0.25rem' }}>{view.id}</p>
              <h2 style={{ fontSize: '1.4rem' }}>{view.title}</h2>
            </div>
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <Badge type={view.status === 'FINALIZED' ? 'active' : 'warning'}>{view.status}</Badge>
              {view.integrity
                ? <Badge type="active"><CheckCircle size={12} style={{ marginRight: '4px' }} />INTEGRITY VERIFIED</Badge>
                : <Badge type="danger">⚠ TAMPER DETECTED</Badge>
              }
            </div>
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem', marginBottom: '1.5rem', padding: '1rem', background: 'rgba(255,255,255,0.02)', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.05)' }}>
            {[
              { k: 'Flight Session', v: view.sessionId },
              { k: 'Lead Investigator', v: view.investigator },
              { k: 'Created At', v: formatDate(view.createdAt) },
              { k: 'Status', v: view.status },
            ].map((item, i) => (
              <div key={i}>
                <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', color: '#64748b', marginBottom: '0.2rem' }}>{item.k}</p>
                <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#f8fafc' }}>{item.v}</p>
              </div>
            ))}
          </div>
          <h4 style={{ color: '#94a3b8', fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: '0.75rem' }}>Executive Summary</h4>
          <p style={{ color: '#cbd5e1', lineHeight: 1.8, fontSize: '0.95rem' }}>{view.summary}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <h1 className="page-title">Investigation Reports</h1>
      <p className="page-subtitle">Formal safety investigation and post-flight audit documentation.</p>

      {/* KPI Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginBottom: '1.5rem' }}>
        {[
          { label: 'Total Reports', value: MOCK_REPORTS.length, color: '#38bdf8' },
          { label: 'Draft', value: MOCK_REPORTS.filter(r => r.status === 'DRAFT').length, color: '#f59e0b' },
          { label: 'Finalized', value: MOCK_REPORTS.filter(r => r.status === 'FINALIZED').length, color: '#10b981' },
        ].map((s, i) => (
          <div key={i} className="glass-panel" style={{ padding: '1rem 1.25rem' }}>
            <p style={{ fontSize: '0.75rem', color: '#64748b', textTransform: 'uppercase' }}>{s.label}</p>
            <h3 style={{ fontSize: '1.75rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: s.color }}>{s.value}</h3>
          </div>
        ))}
      </div>

      {/* New Report Button */}
      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '1.25rem' }}>
        <button style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.6rem 1.25rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 600, fontSize: '0.875rem', cursor: 'pointer' }}>
          <Plus size={16} /> New Investigation Report
        </button>
      </div>

      {MOCK_REPORTS.length === 0 ? (
        <EmptyState title="No Reports" description="No investigation reports have been filed yet." />
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {MOCK_REPORTS.map(report => (
            <div key={report.id} className="glass-panel" style={{ padding: '1.25rem 1.5rem', borderLeft: `4px solid ${report.integrity ? '#10b981' : '#f43f5e'}`, cursor: 'pointer' }} onClick={() => setView(report)}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '0.75rem' }}>
                <div>
                  <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: '#38bdf8' }}>{report.id}</span>
                  <h3 style={{ fontSize: '1.05rem', marginTop: '0.25rem' }}>{report.title}</h3>
                </div>
                <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                  <Badge type={report.status === 'FINALIZED' ? 'active' : 'warning'}>{report.status}</Badge>
                  <Badge type={report.integrity ? 'active' : 'danger'}>{report.integrity ? 'INTEGRITY OK' : '⚠ TAMPERED'}</Badge>
                </div>
              </div>
              <p style={{ color: '#94a3b8', fontSize: '0.85rem', lineHeight: 1.6, marginBottom: '0.75rem' }}>
                {report.summary.length > 160 ? report.summary.substring(0, 160) + '…' : report.summary}
              </p>
              <div style={{ display: 'flex', gap: '1.5rem', fontSize: '0.78rem', color: '#64748b' }}>
                <span>Investigator: <span style={{ color: '#94a3b8' }}>{report.investigator}</span></span>
                <span>Filed: <span style={{ fontFamily: 'var(--font-mono)', color: '#94a3b8' }}>{formatDate(report.createdAt)}</span></span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default InvestigationReports;
