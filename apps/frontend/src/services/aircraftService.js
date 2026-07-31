import apiClient from './api';

export const fetchAllAircraft = async () => {
  try {
    const response = await apiClient.get('/aircraft');
    return response.data;
  } catch (error) {
    return null;
  }
};
