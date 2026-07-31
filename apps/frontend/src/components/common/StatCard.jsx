import React from 'react';

const StatCard = ({ title, value, icon: Icon, trend, color = '#38bdf8' }) => {
  return (
    <div className="glass-panel" style={{ padding: '1.25rem' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
        <div>
          <p style={{ fontSize: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: '#64748b', marginBottom: '0.25rem' }}>
            {title}
          </p>
          <h3 style={{ fontSize: '1.75rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: '#f8fafc' }}>
            {value}
          </h3>
        </div>
        {Icon && (
          <div style={{
            padding: '0.625rem',
            borderRadius: '10px',
            background: `${color}15`,
            border: `1px solid ${color}30`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}>
            <Icon size={22} color={color} />
          </div>
        )}
      </div>

      {trend && (
        <div style={{ fontSize: '0.75rem', color: trend.startsWith('+') ? '#10b981' : '#f43f5e', fontWeight: 500 }}>
          {trend} <span style={{ color: '#64748b' }}>vs last 24h</span>
        </div>
      )}
    </div>
  );
};

export default StatCard;
