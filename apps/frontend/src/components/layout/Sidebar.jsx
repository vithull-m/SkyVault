import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  Plane,
  Radio,
  Activity,
  ShieldAlert,
  Link,
  FileSearch,
  User,
  Settings,
  ShieldCheck
} from 'lucide-react';

const Sidebar = () => {
  const navItems = [
    { label: 'Dashboard', path: '/', icon: LayoutDashboard },
    { label: 'Aircraft Fleet', path: '/aircraft', icon: Plane },
    { label: 'Flight Sessions', path: '/sessions', icon: Radio },
    { label: 'Live Telemetry', path: '/live-telemetry', icon: Activity },
    { label: 'AI Anomaly Alerts', path: '/ai-alerts', icon: ShieldAlert },
    { label: 'Blockchain Verification', path: '/blockchain', icon: Link },
    { label: 'Investigation Reports', path: '/investigations', icon: FileSearch },
    { label: 'User Profile', path: '/profile', icon: User },
    { label: 'Settings', path: '/settings', icon: Settings },
  ];

  return (
    <aside className="sidebar">
      {/* Brand Header */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '2.5rem', paddingLeft: '0.5rem' }}>
        <div style={{
          width: '38px',
          height: '38px',
          borderRadius: '10px',
          background: 'linear-gradient(135deg, #38bdf8 0%, #3b82f6 100%)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          boxShadow: '0 0 15px rgba(56, 189, 248, 0.4)'
        }}>
          <ShieldCheck size={22} color="#ffffff" />
        </div>
        <div>
          <h2 style={{ fontSize: '1.2rem', fontWeight: 700, letterSpacing: '-0.03em', color: '#ffffff' }}>SkyVault</h2>
          <p style={{ fontSize: '0.65rem', color: '#38bdf8', textTransform: 'uppercase', letterSpacing: '0.1em' }}>Cloud Black Box</p>
        </div>
      </div>

      {/* Navigation List */}
      <nav style={{ display: 'flex', flexDirection: 'column', gap: '0.35rem' }}>
        {navItems.map((item) => {
          const Icon = item.icon;
          return (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) => (isActive ? 'active-nav' : 'nav-link')}
              style={({ isActive }) => ({
                display: 'flex',
                alignItems: 'center',
                gap: '0.875rem',
                padding: '0.75rem 1rem',
                borderRadius: '8px',
                color: isActive ? '#38bdf8' : '#94a3b8',
                backgroundColor: isActive ? 'rgba(56, 189, 248, 0.1)' : 'transparent',
                borderLeft: isActive ? '3px solid #38bdf8' : '3px solid transparent',
                textDecoration: 'none',
                fontSize: '0.9rem',
                fontWeight: isActive ? 600 : 400,
                transition: 'all 0.2s ease',
              })}
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </NavLink>
          );
        })}
      </nav>

      {/* Footer System Status */}
      <div style={{ marginTop: 'auto', padding: '1rem', background: 'rgba(255,255,255,0.02)', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.05)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.25rem' }}>
          <span className="live-indicator"></span>
          <span style={{ fontSize: '0.75rem', fontWeight: 600, color: '#10b981' }}>SYSTEM ONLINE</span>
        </div>
        <p style={{ fontSize: '0.7rem', color: '#64748b' }}>PostgreSQL & Hardhat EVM Sync</p>
      </div>
    </aside>
  );
};

export default Sidebar;
