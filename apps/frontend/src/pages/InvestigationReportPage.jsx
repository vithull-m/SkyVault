import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Badge from '../components/common/Badge';
import { ShieldCheck, Printer, ArrowLeft } from 'lucide-react';

const InvestigationReportPage = () => {
  const { flightId } = useParams();
  const navigate = useNavigate();

  const reportDate = new Date().toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });

  const handlePrint = () => {
    window.print();
  };

  return (
    <div className="page-container">
      {/* Top Action Bar */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <button onClick={() => navigate(`/investigation/detail/${flightId || 'FL-2026-0042'}`)} style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', background: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', padding: '0.5rem 1rem', borderRadius: '8px', cursor: 'pointer', fontSize: '0.85rem' }}>
          <ArrowLeft size={16} /> Back to Case File
        </button>

        <button onClick={handlePrint} style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', padding: '0.6rem 1.25rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 600, fontSize: '0.875rem', cursor: 'pointer' }}>
          <Printer size={16} /> Print / Export Official PDF Report
        </button>
      </div>

      {/* Official Report Document */}
      <div className="glass-panel" style={{ padding: '2.5rem', background: '#0b1120', border: '1px solid rgba(255,255,255,0.1)' }}>
        {/* Document Header */}
        <div style={{ borderBottom: '2px solid rgba(56,189,248,0.3)', paddingBottom: '1.5rem', marginBottom: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
          <div>
            <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.12em', color: '#38bdf8', fontWeight: 700 }}>Government Investigation Agency</p>
            <h1 style={{ fontSize: '1.6rem', marginTop: '0.25rem', marginBottom: '0.25rem', color: '#ffffff' }}>Official Air Safety Investigation Report</h1>
            <p style={{ fontSize: '0.85rem', color: '#64748b' }}>SkyVault Cloud Black Box Forensic Audit Summary</p>
          </div>
          <div style={{ textAlign: 'right' }}>
            <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.85rem', color: '#38bdf8', fontWeight: 600 }}>INV-REP-{flightId || 'FL-2026-0042'}</p>
            <p style={{ fontSize: '0.75rem', color: '#94a3b8' }}>{reportDate}</p>
            <span style={{ fontSize: '0.7rem', background: 'rgba(244,63,94,0.15)', color: '#f43f5e', padding: '0.2rem 0.5rem', borderRadius: '4px', display: 'inline-block', marginTop: '0.35rem', fontWeight: 600 }}>
              OFFICIAL / SAFETY CONFIDENTIAL
            </span>
          </div>
        </div>

        {/* Section 1: Flight & Aircraft Details */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1rem', color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1rem', borderBottom: '1px solid rgba(255,255,255,0.08)', paddingBottom: '0.35rem' }}>
            1. Flight & Aircraft Information
          </h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '1rem' }}>
            {[
              { k: 'Flight ID', v: flightId || 'FL-2026-0042' },
              { k: 'Aircraft Tail Number', v: 'N737SV' },
              { k: 'Aircraft Model', v: 'Boeing 737-800' },
              { k: 'Airline Operator', v: 'SkyVault Airways' },
              { k: 'Flight Route', v: 'KJFK (New York) ➔ EGLL (London)' },
              { k: 'Recorded Telemetry Frames', v: '18,432 frames' },
            ].map((item, i) => (
              <div key={i}>
                <p style={{ fontSize: '0.72rem', textTransform: 'uppercase', color: '#64748b' }}>{item.k}</p>
                <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.9rem', color: '#f8fafc', fontWeight: 600 }}>{item.v}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Section 2: Timeline of Events */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1rem', color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1rem', borderBottom: '1px solid rgba(255,255,255,0.08)', paddingBottom: '0.35rem' }}>
            2. Timeline of Chronological Events
          </h3>
          <ul style={{ color: '#cbd5e1', fontSize: '0.88rem', lineHeight: 1.8, paddingLeft: '1.25rem' }}>
            <li>08:14:00 UTC – Flight session FL-2026-0042 initialized at KJFK terminal. Flaps set to 15°, taxi approved.</li>
            <li>08:15:30 UTC – Takeoff phase initiated on Runway 13L. Airspeed Vr (145 kts) attained; climb phase engaged.</li>
            <li>08:22:00 UTC – Aircraft reached cruise altitude 33,000 ft at Mach 0.78 (450 kts indicated airspeed). Autopilot engaged.</li>
            <li>09:12:14 UTC – Unscheduled rapid altitude descent of 1,800 ft logged within 32 seconds. Vertical speed reached -3,800 fpm. AI Anomaly Alert triggered.</li>
            <li>09:12:46 UTC – Crew regained steady altitude control at 31,200 ft. Flight path stabilized.</li>
            <li>12:44:00 UTC – Normal arrival and touchdown completed on Runway 27R at EGLL.</li>
          </ul>
        </div>

        {/* Section 3: AI Findings */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1rem', color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1rem', borderBottom: '1px solid rgba(255,255,255,0.08)', paddingBottom: '0.35rem' }}>
            3. Artificial Intelligence Diagnostic Findings
          </h3>
          <p style={{ color: '#cbd5e1', fontSize: '0.88rem', lineHeight: 1.7 }}>
            The SkyVault AI Anomaly Detection service evaluated all 18,432 telemetry frames in real-time. One high-severity anomaly event was flagged: <strong>RAPID_ALTITUDE_DROP</strong> (Severity Score: 0.87, Model Confidence: 94%). Diagnostic feature attribution correlates the altitude deviation with sudden severe wake turbulence encounter rather than mechanical engine failure.
          </p>
        </div>

        {/* Section 4: Blockchain Integrity Verification Result */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{ fontSize: '1rem', color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1rem', borderBottom: '1px solid rgba(255,255,255,0.08)', paddingBottom: '0.35rem' }}>
            4. Cryptographic Blockchain Data Integrity Result
          </h3>
          <div style={{ padding: '1rem', background: 'rgba(16,185,129,0.08)', border: '1px solid rgba(16,185,129,0.2)', borderRadius: '8px', display: 'flex', alignItems: 'center', gap: '0.875rem' }}>
            <ShieldCheck size={28} color="#10b981" />
            <div>
              <h4 style={{ color: '#10b981', fontSize: '0.95rem' }}>Status: 100% Cryptographically Intact</h4>
              <p style={{ color: '#94a3b8', fontSize: '0.82rem' }}>Every telemetry frame SHA-256 hash matches Merkle tree root anchors stored on Ethereum Smart Contract `0x742d35Cc6634C0532925a3b8D4C9Ef6d1eA7c2f0`.</p>
            </div>
          </div>
        </div>

        {/* Section 5: Final Investigation Notes & Sign-Off */}
        <div>
          <h3 style={{ fontSize: '1rem', color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '1rem', borderBottom: '1px solid rgba(255,255,255,0.08)', paddingBottom: '0.35rem' }}>
            5. Final Investigation Notes & Sign-Off
          </h3>
          <p style={{ color: '#cbd5e1', fontSize: '0.88rem', lineHeight: 1.7, marginBottom: '2rem' }}>
            Forensic analysis confirms the flight recorder telemetry is authentic and untampered. The temporary altitude drop was caused by atmospheric turbulence. No structural damage or aircraft system fault was identified. Aircraft N737SV is cleared for continued airworthiness.
          </p>

          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', paddingTop: '1.5rem', borderTop: '1px dashed rgba(255,255,255,0.1)' }}>
            <div>
              <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', color: '#64748b' }}>Primary Investigator</p>
              <p style={{ fontWeight: 600, color: '#f8fafc', fontSize: '0.95rem' }}>Cmdr. James Reeves</p>
              <p style={{ fontSize: '0.75rem', color: '#38bdf8' }}>Government Investigation Agency</p>
            </div>

            <div style={{ textAlign: 'right' }}>
              <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: '#10b981' }}>DIGITALLY SIGNED & VERIFIED</p>
              <p style={{ fontSize: '0.72rem', color: '#64748b' }}>SkyVault Public Key Infrastructure</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InvestigationReportPage;
