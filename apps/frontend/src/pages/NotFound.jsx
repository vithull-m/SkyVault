import React from 'react';
import { Link } from 'react-router-dom';
import { Home } from 'lucide-react';

const NotFound = () => (
  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '60vh', textAlign: 'center', padding: '2rem' }}>
    <div style={{ fontSize: '7rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: 'rgba(56,189,248,0.15)', lineHeight: 1 }}>404</div>
    <h2 style={{ fontSize: '1.5rem', marginTop: '1rem', marginBottom: '0.5rem' }}>Flight Plan Not Found</h2>
    <p style={{ color: '#94a3b8', marginBottom: '2rem', maxWidth: '400px' }}>The page you are requesting does not exist in the SkyVault navigation database.</p>
    <Link to="/" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.65rem 1.5rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', borderRadius: '8px', textDecoration: 'none', fontWeight: 600, fontSize: '0.9rem' }}>
      <Home size={16} /> Return to Base
    </Link>
  </div>
);

export default NotFound;
