import React from 'react';
import { Search, Bell, UserCheck, Shield } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const TopNavbar = () => {
  const { user } = useAuth();

  return (
    <header className="top-navbar">
      {/* Search Input */}
      <div style={{ position: 'relative', width: '320px' }}>
        <Search size={16} color="#64748b" style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)' }} />
        <input
          type="text"
          placeholder="Search flight ID, tail number, or hashes..."
          style={{
            width: '100%',
            padding: '0.5rem 1rem 0.5rem 2.25rem',
            borderRadius: '20px',
            background: 'rgba(255, 255, 255, 0.05)',
            border: '1px solid rgba(255, 255, 255, 0.1)',
            color: '#f8fafc',
            fontSize: '0.85rem',
            outline: 'none',
          }}
        />
      </div>

      {/* Right Controls & Profile */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '1.25rem' }}>
        <div style={{ position: 'relative', cursor: 'pointer' }}>
          <Bell size={20} color="#94a3b8" />
          <span style={{
            position: 'absolute',
            top: '-4px',
            right: '-4px',
            width: '8px',
            height: '8px',
            borderRadius: '50%',
            backgroundColor: '#f43f5e'
          }}></span>
        </div>

        <div style={{ height: '24px', width: '1px', backgroundColor: 'rgba(255,255,255,0.1)' }}></div>

        {/* User Card */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
          <div style={{
            width: '36px',
            height: '36px',
            borderRadius: '50%',
            background: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontWeight: 600,
            fontSize: '0.85rem',
            color: '#ffffff'
          }}>
            AJ
          </div>
          <div>
            <div style={{ fontSize: '0.85rem', fontWeight: 600, color: '#f8fafc' }}>{user?.name || 'Capt. Alex Johnson'}</div>
            <div style={{ fontSize: '0.7rem', color: '#38bdf8', display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
              <Shield size={10} />
              {user?.roleLabel || 'System Administrator'}
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default TopNavbar;
