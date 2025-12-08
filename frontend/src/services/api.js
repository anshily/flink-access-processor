import axiosInstance from '../utils/axiosConfig';

export const fetchAlertRecords = async () => {
  try {
    const response = await axiosInstance.get('/alerts');
    return response.data;
  } catch (error) {
    console.error('Error fetching alert records:', error);
    throw error;
  }
};

export const fetchAlertRecordsByEmployee = async (employeeId) => {
  try {
    const response = await axiosInstance.get(`/alerts/employee/${employeeId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching alert records for employee ${employeeId}:`, error);
    throw error;
  }
};

export const fetchConsecutiveWorkDays = async () => {
  try {
    const response = await axiosInstance.get('/consecutive-days');
    return response.data;
  } catch (error) {
    console.error('Error fetching consecutive work days:', error);
    throw error;
  }
};

export const fetchConsecutiveWorkDaysByEmployee = async (employeeId) => {
  try {
    const response = await axiosInstance.get(`/consecutive-days/employee/${employeeId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching consecutive work days for employee ${employeeId}:`, error);
    throw error;
  }
};

export const fetchAccessRecordsByEmployee = async (employeeId) => {
  try {
    const response = await axiosInstance.get(`/access-records/employee/${employeeId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching access records for employee ${employeeId}:`, error);
    throw error;
  }
};