import apiClient from './api';

export const fetchLatestTelemetry = async (flightId) => {
  try {
    const response = await apiClient.get(`/telemetry/latest/${flightId}`);
    return response.data;
  } catch (error) {
    console.warn('Backend offline or simulated data fallback for live telemetry');
    return null;
  }
};

export const fetchTelemetryByFlightId = async (flightId, page = 0, size = 50) => {
  try {
    const response = await apiClient.get(`/telemetry/flight/${flightId}?pageNo=${page}&pageSize=${size}`);
    return response.data;
  } catch (error) {
    return null;
  }
};
