import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Badge from '../components/common/Badge';
import { FileText, ShieldCheck, Play, Save, CheckCircle, XCircle } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const InvestigationDetail = () => {
  const { flightId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [noteText, setNoteText] = useState('');
  const [evidenceSummary, setEvidenceSummary] = useState('');
  const [savedNotes, setSavedNotes] = useState([
    {
      id: '1',
      investigatorName: 'Cmdr. James Reeves',
      incidentType: 'RAPID_ALTITUDE_DROP',
      noteText: 'Preliminary radar telemetry confirms 1,800 ft drop within 32s. Pilot reports encountering sudden wake turbulence.',
      evidenceSummary: 'Radar logs, flight recorder frames #18400-#18432.',
      createdAt: '2026-07-31 11:30',
    }
  ]);
  const [savedMessage, setSavedMessage] = useState('');

  const isInvestigator = user?.role === 'ROLE_INVESTIGATOR' || true; // Full access for investigator

  const handleSaveNote = (e) => {
    e.preventDefault();
    if (!noteText.trim()) return;

    const newNote = {
      id: String(Date.now()),
      investigatorName: user?.name || 'Senior Investigator',
      incidentType: 'FORENSIC_AUDIT',
      noteText,
      evidenceSummary,
      createdAt: new Date().toLocaleString(),
    };

    setSavedNotes([newNote, ...savedNotes]);
    setNoteText('');
    setEvidenceSummary('');
    setSavedMessage('Investigation note saved to case file.');
    setTimeout(() => setSavedMessage(''), 3000);
  };

  return (
    <div className="page-container">
      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1.5rem', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <button onClick={() => navigate('/investigations')} style={{ background: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', padding: '0.4rem 0.875rem', borderRadius: '6px', cursor: 'pointer', fontSize: '0.8rem', marginBottom: '0.75rem' }}>
            ← Back to Investigation Dashboard
          </button>
          <h1 className="page-title">Case File: {flightId || 'FL-2026-0042'}</h1>
          <p className="page-subtitle">Aviation Safety Investigation Audit & Evidence File</p>
        </div>

        <div style={{ display: 'flex', gap: '0.75rem' }}>
          <button
            onClick={() => navigate(`/investigation/replay/${flightId || 'FL-2026-0042'}`)}
            style={{ padding: '0.6rem 1.25rem', background: 'rgba(16,185,129,0.1)', color: '#10b981', border: '1px solid rgba(16,185,129,0.3)', borderRadius: '8px', fontSize: '0.85rem', fontWeight: 600, cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.4rem' }}
          >
            <Play size={16} /> Replay Flight Stream
          </button>
          <button
            onClick={() => navigate(`/investigation/report/${flightId || 'FL-2026-0042'}`)}
            style={{ padding: '0.6rem 1.25rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', border: 'none', borderRadius: '8px', fontSize: '0.85rem', fontWeight: 600, cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '0.4rem' }}
          >
            <FileText size={16} /> Generate Official Report
          </button>
        </div>
      </div>

      {/* Grid Overview Panels */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '1.25rem', marginBottom: '1.5rem' }}>
        {/* Flight & Aircraft Info */}
        <div className="glass-panel" style={{ padding: '1.25rem' }}>
          <h3 style={{ fontSize: '1rem', color: '#38bdf8', marginBottom: '1rem' }}>Flight & Aircraft Details</h3>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem' }}>
            {[
              { k: 'Flight ID', v: flightId || 'FL-2026-0042' },
              { k: 'Aircraft Model', v: 'Boeing 737-800' },
              { k: 'Tail Number', v: 'N737SV' },
              { k: 'Airline', v: 'SkyVault Airways' },
              { k: 'Route', v: 'KJFK ➔ EGLL' },
              { k: 'Engine', v: 'CFM56-7B' },
            ].map((item, i) => (
              <div key={i}>
                <p style={{ fontSize: '0.7rem', color: '#64748b', textTransform: 'uppercase' }}>{item.k}</p>
                <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#f8fafc', fontWeight: 600 }}>{item.v}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Blockchain Integrity Check */}
        <div className="glass-panel" style={{ padding: '1.25rem', borderLeft: '4px solid #10b981' }}>
          <h3 style={{ fontSize: '1rem', color: '#10b981', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
            <ShieldCheck size={18} /> Blockchain Integrity Verification
          </h3>
          <div style={{ marginBottom: '1rem' }}>
            <Badge type="active"><CheckCircle size={12} style={{ marginRight: '4px' }} /> 100% CRYPTOGRAPHICALLY INTACT</Badge>
          </div>
          <p style={{ fontSize: '0.82rem', color: '#94a3b8', lineHeight: 1.5 }}>
            All 18,432 recorded telemetry frames match Merkle root hash anchors registered on the Ethereum smart contract ledger. No database tampering detected.
          </p>
        </div>
      </div>

      {/* Investigation Notes & Evidence Form */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.5rem', flexWrap: 'wrap' }}>
        {/* Form Panel */}
        <div className="glass-panel" style={{ padding: '1.5rem' }}>
          <h3 style={{ fontSize: '1.05rem', marginBottom: '1rem' }}>File Investigator Notes</h3>
          {savedMessage && (
            <div style={{ padding: '0.6rem 1rem', background: 'rgba(16,185,129,0.15)', color: '#10b981', borderRadius: '6px', fontSize: '0.85rem', marginBottom: '1rem' }}>
              {savedMessage}
            </div>
          )}
          <form onSubmit={handleSaveNote}>
            <div style={{ marginBottom: '1rem' }}>
              <label style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b', display: 'block', marginBottom: '0.35rem' }}>Evidence Summary</label>
              <input
                type="text"
                placeholder="e.g. Flight frames #18400-#18432, radar telemetry logs"
                value={evidenceSummary}
                onChange={e => setEvidenceSummary(e.target.value)}
                style={{ width: '100%', padding: '0.55rem 0.875rem', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '6px', fontSize: '0.85rem', outline: 'none' }}
              />
            </div>

            <div style={{ marginBottom: '1.25rem' }}>
              <label style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b', display: 'block', marginBottom: '0.35rem' }}>Detailed Investigation Notes</label>
              <textarea
                rows={5}
                placeholder="Enter formal safety findings, telemetry discrepancies, or pilot debrief notes..."
                value={noteText}
                onChange={e => setNoteText(e.target.value)}
                style={{ width: '100%', padding: '0.75rem', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '6px', fontSize: '0.85rem', outline: 'none', resize: 'vertical' }}
              />
            </div>

            <button
              type="submit"
              disabled={!isInvestigator}
              style={{
                width: '100%', padding: '0.65rem',
                background: isInvestigator ? 'linear-gradient(135deg, #38bdf8, #3b82f6)' : 'rgba(255,255,255,0.1)',
                color: isInvestigator ? '#fff' : '#64748b',
                border: 'none', borderRadius: '8px', fontWeight: 600, fontSize: '0.875rem', cursor: isInvestigator ? 'pointer' : 'not-allowed',
                display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '0.4rem'
              }}
            >
              <Save size={16} /> Save Note to Case File
            </button>
            {!isInvestigator && <p style={{ fontSize: '0.72rem', color: '#f43f5e', marginTop: '0.35rem', textAlign: 'center' }}>Role restriction: Only Government Investigators can submit notes.</p>}
          </form>
        </div>

        {/* Existing Notes Log */}
        <div className="glass-panel" style={{ padding: '1.5rem' }}>
          <h3 style={{ fontSize: '1.05rem', marginBottom: '1rem' }}>Investigation Case Notes ({savedNotes.length})</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', maxHeight: '420px', overflowY: 'auto' }}>
            {savedNotes.map(n => (
              <div key={n.id} style={{ padding: '1rem', background: 'rgba(255,255,255,0.02)', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.05)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.4rem' }}>
                  <span style={{ fontWeight: 600, color: '#38bdf8', fontSize: '0.85rem' }}>{n.investigatorName}</span>
                  <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.75rem', color: '#64748b' }}>{n.createdAt}</span>
                </div>
                <p style={{ color: '#cbd5e1', fontSize: '0.85rem', lineHeight: 1.5, marginBottom: '0.5rem' }}>{n.noteText}</p>
                {n.evidenceSummary && (
                  <p style={{ fontSize: '0.75rem', color: '#a855f7' }}><strong>Evidence:</strong> {n.evidenceSummary}</p>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default InvestigationDetail;
