import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Sidebar from './components/layout/Sidebar';
import TopNavbar from './components/layout/TopNavbar';

// Page Imports
import DashboardOverview from './pages/DashboardOverview';
import AircraftFleet from './pages/AircraftFleet';
import FlightSessions from './pages/FlightSessions';
import LiveTelemetry from './pages/LiveTelemetry';
import AiAlerts from './pages/AiAlerts';
import BlockchainVerification from './pages/BlockchainVerification';
import InvestigationReports from './pages/InvestigationReports';
import UserProfile from './pages/UserProfile';
import Settings from './pages/Settings';
import NotFound from './pages/NotFound';

// Investigation Module Specific Pages
import InvestigationDashboard from './pages/InvestigationDashboard';
import InvestigationDetail from './pages/InvestigationDetail';
import FlightReplayPage from './pages/FlightReplayPage';
import InvestigationReportPage from './pages/InvestigationReportPage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="app-container">
          <Sidebar />
          <div className="main-content">
            <TopNavbar />
            <Routes>
              <Route path="/" element={<DashboardOverview />} />
              <Route path="/aircraft" element={<AircraftFleet />} />
              <Route path="/sessions" element={<FlightSessions />} />
              <Route path="/live-telemetry" element={<LiveTelemetry />} />
              <Route path="/ai-alerts" element={<AiAlerts />} />
              <Route path="/blockchain" element={<BlockchainVerification />} />
              <Route path="/investigations" element={<InvestigationDashboard />} />
              <Route path="/investigation/detail/:flightId" element={<InvestigationDetail />} />
              <Route path="/investigation/replay/:flightId" element={<FlightReplayPage />} />
              <Route path="/investigation/report/:flightId" element={<InvestigationReportPage />} />
              <Route path="/reports-summary" element={<InvestigationReports />} />
              <Route path="/profile" element={<UserProfile />} />
              <Route path="/settings" element={<Settings />} />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </div>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
