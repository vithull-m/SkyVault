import React from 'react';
import Badge from '../components/common/Badge';
import { Link, CheckCircle, XCircle, ExternalLink } from 'lucide-react';
import { truncateHash } from '../utils/formatters';

const MOCK_BLOCKS = [
  { id: 'VER-001', flightId: 'FL-2026-0042', merkleRoot: '0x4a3b2c1d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b7c6d5e4f3a2b', txHash: '0xa1b2c3d4e5f6789012345678901234567890abcdef', blockNumber: 12483920, contract: '0x742d35Cc6634C0532925a3b8D4C9Ef6d1eA7c2f0', status: 'VERIFIED_ON_CHAIN', anchoredAt: '2026-07-31T08:20:00Z', startBlock: '2026-07-31T08:14:00Z', endBlock: '2026-07-31T08:19:59Z' },
  { id: 'VER-002', flightId: 'FL-2026-0041', merkleRoot: '0x9f8e7d6c5b4a3d2c1b0a9e8d7c6b5a4f3e2d1c0b9a8f7e6d5c4b3a2f1e0d9c8b', txHash: '0xf9e8d7c6b5a4f3e2d1c0b9a8f7e6d5c4b3a2f1e0', blockNumber: 12483889, contract: '0x742d35Cc6634C0532925a3b8D4C9Ef6d1eA7c2f0', status: 'VERIFIED_ON_CHAIN', anchoredAt: '2026-07-31T10:00:00Z', startBlock: '2026-07-31T09:55:00Z', endBlock: '2026-07-31T09:59:59Z' },
  { id: 'VER-003', flightId: 'FL-2026-0038', merkleRoot: '0x1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1a2b', txHash: '0x3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d', blockNumber: 12483720, contract: '0x742d35Cc6634C0532925a3b8D4C9Ef6d1eA7c2f0', status: 'TAMPER_DETECTED', anchoredAt: '2026-07-31T05:50:00Z', startBlock: '2026-07-31T05:44:00Z', endBlock: '2026-07-31T05:49:59Z' },
];

const BlockchainVerification = () => {
  const formatDate = (iso) => new Date(iso).toLocaleString('en-US', { month: 'short', day: '2-digit', hour: '2-digit', minute: '2-digit' });
  const verified = MOCK_BLOCKS.filter(b => b.status === 'VERIFIED_ON_CHAIN').length;
  const tampered = MOCK_BLOCKS.filter(b => b.status === 'TAMPER_DETECTED').length;

  return (
    <div className="page-container">
      <h1 className="page-title">Blockchain Integrity Verification</h1>
      <p className="page-subtitle">Merkle root anchors and Ethereum smart contract transaction verification records.</p>

      {/* KPI Strip */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '1rem', marginBottom: '1.5rem' }}>
        {[
          { label: 'Total Anchors', value: MOCK_BLOCKS.length, color: '#38bdf8' },
          { label: 'Verified On-Chain', value: verified, color: '#10b981' },
          { label: 'Tamper Detected', value: tampered, color: '#f43f5e' },
          { label: 'Network', value: 'Ethereum', color: '#a855f7' },
        ].map((s, i) => (
          <div key={i} className="glass-panel" style={{ padding: '1rem 1.25rem' }}>
            <p style={{ fontSize: '0.75rem', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.05em' }}>{s.label}</p>
            <h3 style={{ fontSize: '1.5rem', fontWeight: 700, fontFamily: 'var(--font-mono)', color: s.color }}>{s.value}</h3>
          </div>
        ))}
      </div>

      {/* Contract Info Banner */}
      <div className="glass-panel" style={{ padding: '1rem 1.5rem', marginBottom: '1.5rem', borderLeft: '4px solid #a855f7', display: 'flex', gap: '2rem', flexWrap: 'wrap' }}>
        <div>
          <p style={{ fontSize: '0.7rem', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.25rem' }}>Smart Contract Address</p>
          <span style={{ fontFamily: 'var(--font-mono)', fontSize: '0.9rem', color: '#a855f7' }}>0x742d35Cc6634C0532925a3b8D4C9Ef6d1eA7c2f0</span>
        </div>
        <div>
          <p style={{ fontSize: '0.7rem', color: '#64748b', textTransform: 'uppercase', letterSpacing: '0.05em', marginBottom: '0.25rem' }}>Network</p>
          <span style={{ fontFamily: 'var(--font-mono)', color: '#f8fafc' }}>Ethereum Mainnet (Chain ID: 1)</span>
        </div>
      </div>

      {/* Verification Records */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
        {MOCK_BLOCKS.map(block => {
          const isVerified = block.status === 'VERIFIED_ON_CHAIN';
          const borderColor = isVerified ? '#10b981' : '#f43f5e';
          return (
            <div key={block.id} className="glass-panel" style={{ padding: '1.25rem 1.5rem', borderLeft: `4px solid ${borderColor}` }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem', flexWrap: 'wrap', gap: '0.5rem' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.875rem' }}>
                  {isVerified
                    ? <CheckCircle size={20} color="#10b981" />
                    : <XCircle size={20} color="#f43f5e" />
                  }
                  <span style={{ fontFamily: 'var(--font-mono)', fontWeight: 700, color: '#f8fafc' }}>{block.id}</span>
                  <Badge type={isVerified ? 'active' : 'danger'}>
                    {isVerified ? 'VERIFIED ON-CHAIN' : '⚠ TAMPER DETECTED'}
                  </Badge>
                </div>
                <a href="#" style={{ display: 'flex', alignItems: 'center', gap: '0.35rem', color: '#38bdf8', fontSize: '0.8rem', textDecoration: 'none' }}>
                  <ExternalLink size={14} /> View on Etherscan
                </a>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '1rem' }}>
                {[
                  { k: 'Flight Session', v: block.flightId },
                  { k: 'Block Number', v: `#${block.blockNumber.toLocaleString()}` },
                  { k: 'Anchored At', v: formatDate(block.anchoredAt) },
                  { k: 'Telemetry Window', v: `${formatDate(block.startBlock)} → ${formatDate(block.endBlock)}` },
                ].map((item, i) => (
                  <div key={i}>
                    <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: '#64748b', marginBottom: '0.2rem' }}>{item.k}</p>
                    <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.82rem', color: '#f8fafc' }}>{item.v}</p>
                  </div>
                ))}

                <div style={{ gridColumn: '1 / -1' }}>
                  <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: '#64748b', marginBottom: '0.2rem' }}>Merkle Root Hash</p>
                  <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.78rem', color: '#a855f7', wordBreak: 'break-all' }}>{block.merkleRoot}</p>
                </div>
                <div style={{ gridColumn: '1 / -1' }}>
                  <p style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.05em', color: '#64748b', marginBottom: '0.2rem' }}>Transaction Hash</p>
                  <p style={{ fontFamily: 'var(--font-mono)', fontSize: '0.78rem', color: '#38bdf8', wordBreak: 'break-all' }}>{block.txHash}</p>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default BlockchainVerification;
