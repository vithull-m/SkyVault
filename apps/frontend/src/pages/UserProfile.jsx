import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Shield, Mail, User, Clock, Key } from 'lucide-react';

const UserProfile = () => {
  const { user } = useAuth();

  const profileFields = [
    { icon: User, label: 'Full Name', value: user?.name || 'Capt. Alex Johnson' },
    { icon: Mail, label: 'Email Address', value: user?.email || 'alex.johnson@skyvault.aero' },
    { icon: Shield, label: 'Assigned Role', value: user?.roleLabel || 'System Administrator' },
    { icon: Clock, label: 'Last Login', value: 'July 31, 2026 – 10:14:02 IST' },
    { icon: Key, label: 'Account Status', value: 'Active' },
  ];

  return (
    <div className="page-container">
      <h1 className="page-title">User Profile</h1>
      <p className="page-subtitle">Your SkyVault account identity and access configuration.</p>

      <div style={{ display: 'grid', gridTemplateColumns: '300px 1fr', gap: '1.5rem', flexWrap: 'wrap' }}>
        {/* Avatar Panel */}
        <div className="glass-panel" style={{ padding: '2rem', display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
          <div style={{
            width: '90px', height: '90px', borderRadius: '50%',
            background: 'linear-gradient(135deg, #38bdf8, #3b82f6)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '2rem', fontWeight: 700, color: '#fff',
            marginBottom: '1rem',
            boxShadow: '0 0 30px rgba(56, 189, 248, 0.4)',
          }}>
            AJ
          </div>
          <h3 style={{ fontSize: '1.1rem' }}>{user?.name || 'Capt. Alex Johnson'}</h3>
          <p style={{ color: '#38bdf8', fontSize: '0.82rem', marginTop: '0.25rem', marginBottom: '1rem' }}>{user?.roleLabel || 'System Administrator'}</p>
          <span style={{ background: 'rgba(16,185,129,0.15)', color: '#10b981', border: '1px solid rgba(16,185,129,0.3)', padding: '0.25rem 0.75rem', borderRadius: '9999px', fontSize: '0.75rem', fontWeight: 600, textTransform: 'uppercase' }}>
            Active
          </span>
          <button style={{ marginTop: '1.5rem', width: '100%', padding: '0.6rem', background: 'rgba(56,189,248,0.1)', color: '#38bdf8', border: '1px solid rgba(56,189,248,0.3)', borderRadius: '8px', fontSize: '0.85rem', cursor: 'pointer', fontWeight: 500 }}>
            Edit Profile
          </button>
        </div>

        {/* Profile Fields */}
        <div className="glass-panel" style={{ padding: '1.75rem' }}>
          <h4 style={{ fontSize: '0.9rem', textTransform: 'uppercase', letterSpacing: '0.07em', color: '#64748b', marginBottom: '1.5rem' }}>Account Details</h4>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
            {profileFields.map((field, i) => {
              const Icon = field.icon;
              return (
                <div key={i} style={{ display: 'flex', gap: '1rem', alignItems: 'center', paddingBottom: '1.25rem', borderBottom: i < profileFields.length - 1 ? '1px solid rgba(255,255,255,0.05)' : 'none' }}>
                  <div style={{ width: '36px', height: '36px', borderRadius: '8px', background: 'rgba(56,189,248,0.1)', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                    <Icon size={17} color="#38bdf8" />
                  </div>
                  <div>
                    <p style={{ fontSize: '0.72rem', textTransform: 'uppercase', color: '#64748b', marginBottom: '0.2rem', letterSpacing: '0.05em' }}>{field.label}</p>
                    <p style={{ color: '#f8fafc', fontWeight: 500, fontSize: '0.95rem' }}>{field.value}</p>
                  </div>
                </div>
              );
            })}
          </div>

          <div style={{ marginTop: '1.5rem' }}>
            <h4 style={{ fontSize: '0.9rem', textTransform: 'uppercase', letterSpacing: '0.07em', color: '#64748b', marginBottom: '1rem' }}>Security</h4>
            <button style={{ padding: '0.6rem 1.25rem', background: 'rgba(244,63,94,0.1)', color: '#f43f5e', border: '1px solid rgba(244,63,94,0.3)', borderRadius: '8px', fontSize: '0.85rem', cursor: 'pointer', fontWeight: 500 }}>
              Change Password
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserProfile;
