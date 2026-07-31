import React from 'react';
import { Inbox } from 'lucide-react';

const EmptyState = ({ title = 'No Records Found', description = 'There are no active flight sessions or telemetry records matching your filter.' }) => {
  return (
    <div className="glass-panel" style={{ textAlign: 'center', padding: '3rem 1.5rem' }}>
      <Inbox size={48} color="#64748b" style={{ marginBottom: '1rem' }} />
      <h3 style={{ fontSize: '1.1rem', marginBottom: '0.5rem', color: '#f8fafc' }}>{title}</h3>
      <p style={{ color: '#94a3b8', fontSize: '0.875rem', maxWidth: '400px', margin: '0 auto' }}>{description}</p>
    </div>
  );
};

export default EmptyState;
