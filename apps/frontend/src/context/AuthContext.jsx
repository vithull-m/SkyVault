import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState({
    username: 'capt_johnson',
    name: 'Capt. Alex Johnson',
    email: 'alex.johnson@skyvault.aero',
    role: 'ROLE_ADMIN',
    roleLabel: 'System Administrator',
  });

  const [isAuthenticated, setIsAuthenticated] = useState(true);

  const login = (userData, token) => {
    localStorage.setItem('skyvault_token', token);
    setUser(userData);
    setIsAuthenticated(true);
  };

  const logout = () => {
    localStorage.removeItem('skyvault_token');
    setUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
