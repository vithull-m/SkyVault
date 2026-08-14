import apiClient from './api';

export const searchInvestigations = async (params) => {
  try {
    const response = await apiClient.get('/investigations/search', { params });
    return response.data;
  } catch (error) {
    return null;
  }
};

export const getInvestigationDetails = async (flightId) => {
  try {
    const response = await apiClient.get(`/investigations/${flightId}`);
    return response.data;
  } catch (error) {
    return null;
  }
};

export const saveInvestigationNote = async (flightId, noteData) => {
  try {
    const response = await apiClient.post(`/investigations/${flightId}/notes`, noteData);
    return response.data;
  } catch (error) {
    return null;
  }
};

export const generateReport = async (flightId) => {
  try {
    const response = await apiClient.get(`/investigations/${flightId}/report`);
    return response.data;
  } catch (error) {
    return null;
  }
};
