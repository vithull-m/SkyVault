import React, { useState } from 'react';
import { Settings as SettingsIcon, Database, Wifi, Bell, Shield } from 'lucide-react';

const SettingRow = ({ label, description, children }) => (
  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingBottom: '1.25rem', borderBottom: '1px solid rgba(255,255,255,0.05)', marginBottom: '1.25rem', flexWrap: 'wrap', gap: '0.75rem' }}>
    <div>
      <p style={{ fontWeight: 500, color: '#f8fafc', marginBottom: '0.2rem' }}>{label}</p>
      <p style={{ fontSize: '0.82rem', color: '#64748b' }}>{description}</p>
    </div>
    {children}
  </div>
);

const Toggle = ({ checked, onChange }) => (
  <button
    onClick={() => onChange(!checked)}
    style={{
      width: '46px', height: '26px', borderRadius: '13px', border: 'none', cursor: 'pointer',
      background: checked ? 'linear-gradient(135deg, #38bdf8, #3b82f6)' : 'rgba(255,255,255,0.1)',
      position: 'relative', transition: 'background 0.3s', flexShrink: 0,
    }}
  >
    <div style={{
      width: '20px', height: '20px', borderRadius: '50%', background: '#fff',
      position: 'absolute', top: '3px',
      left: checked ? '23px' : '3px',
      transition: 'left 0.3s',
    }} />
  </button>
);

const Settings = () => {
  const [settings, setSettings] = useState({
    livePolling: true,
    emailAlerts: true,
    blockchainSync: true,
    devMode: false,
    highSeverityOnly: false,
    telemetryRate: '1000',
    backendUrl: 'http://localhost:8080',
  });

  const toggle = (key) => setSettings(s => ({ ...s, [key]: !s[key] }));

  return (
    <div className="page-container">
      <h1 className="page-title">System Settings</h1>
      <p className="page-subtitle">Configure SkyVault telemetry ingestion, alerting, and integration preferences.</p>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
        {/* Backend Integration */}
        <div className="glass-panel" style={{ padding: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '1.5rem' }}>
            <Database size={18} color="#38bdf8" />
            <h3 style={{ fontSize: '1.05rem' }}>Backend Integration</h3>
          </div>
          <SettingRow label="Backend REST API URL" description="URL of the SkyVault Spring Boot backend service.">
            <input
              type="text"
              value={settings.backendUrl}
              onChange={e => setSettings(s => ({ ...s, backendUrl: e.target.value }))}
              style={{ padding: '0.5rem 0.875rem', background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '8px', fontSize: '0.85rem', outline: 'none', width: '260px', fontFamily: 'var(--font-mono)' }}
            />
          </SettingRow>
          <SettingRow label="Telemetry Polling Rate (ms)" description="Frequency of live telemetry data refresh from the backend API.">
            <select
              value={settings.telemetryRate}
              onChange={e => setSettings(s => ({ ...s, telemetryRate: e.target.value }))}
              style={{ padding: '0.5rem 0.875rem', background: '#1f2937', border: '1px solid rgba(255,255,255,0.1)', color: '#f8fafc', borderRadius: '8px', fontSize: '0.85rem', outline: 'none', cursor: 'pointer' }}
            >
              <option value="500">500ms (High Frequency)</option>
              <option value="1000">1000ms (Real-Time)</option>
              <option value="2000">2000ms (Standard)</option>
              <option value="5000">5000ms (Low Bandwidth)</option>
            </select>
          </SettingRow>
        </div>

        {/* Live Data & Streaming */}
        <div className="glass-panel" style={{ padding: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '1.5rem' }}>
            <Wifi size={18} color="#10b981" />
            <h3 style={{ fontSize: '1.05rem' }}>Live Telemetry Stream</h3>
          </div>
          <SettingRow label="Enable Live Telemetry Polling" description="Automatically refresh telemetry data at the configured interval.">
            <Toggle checked={settings.livePolling} onChange={() => toggle('livePolling')} />
          </SettingRow>
          <SettingRow label="Blockchain Sync" description="Synchronize verification logs with the Ethereum network in real-time.">
            <Toggle checked={settings.blockchainSync} onChange={() => toggle('blockchainSync')} />
          </SettingRow>
        </div>

        {/* Alerts */}
        <div className="glass-panel" style={{ padding: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '1.5rem' }}>
            <Bell size={18} color="#f59e0b" />
            <h3 style={{ fontSize: '1.05rem' }}>Alert Preferences</h3>
          </div>
          <SettingRow label="Email Notifications" description="Receive email alerts for high-severity AI anomaly detections.">
            <Toggle checked={settings.emailAlerts} onChange={() => toggle('emailAlerts')} />
          </SettingRow>
          <SettingRow label="High Severity Alerts Only" description="Suppress medium and low severity AI alerts from the dashboard.">
            <Toggle checked={settings.highSeverityOnly} onChange={() => toggle('highSeverityOnly')} />
          </SettingRow>
        </div>

        {/* Developer */}
        <div className="glass-panel" style={{ padding: '1.5rem' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.6rem', marginBottom: '1.5rem' }}>
            <Shield size={18} color="#a855f7" />
            <h3 style={{ fontSize: '1.05rem' }}>Developer Options</h3>
          </div>
          <SettingRow label="Developer Mode" description="Enable mock data fallback when the backend API is offline for local UI testing.">
            <Toggle checked={settings.devMode} onChange={() => toggle('devMode')} />
          </SettingRow>
        </div>

        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem' }}>
          <button style={{ padding: '0.6rem 1.25rem', background: 'rgba(255,255,255,0.05)', color: '#94a3b8', border: '1px solid rgba(255,255,255,0.1)', borderRadius: '8px', cursor: 'pointer', fontSize: '0.875rem' }}>
            Reset to Defaults
          </button>
          <button style={{ padding: '0.6rem 1.5rem', background: 'linear-gradient(135deg, #38bdf8, #3b82f6)', color: '#fff', border: 'none', borderRadius: '8px', fontWeight: 600, fontSize: '0.875rem', cursor: 'pointer' }}>
            Save Settings
          </button>
        </div>
      </div>
    </div>
  );
};

export default Settings;
