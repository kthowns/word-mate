import React, { createContext, useState } from 'react';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  const login = async (email, password) => {
    // 로그인 로직
    const data = { email }; // 예시
    setUser(data);
  };

  const logout = () => {
    setUser(null);
  };

  const signUp = async (email, password) => {
    // 회원가입 로직: 서버에 사용자 데이터 저장
    // const response = await fetch('/api/signup', { method: 'POST', body: JSON.stringify({ email, password }) });
    // const data = await response.json();
    const data = { email }; // 예시
    setUser(data); // 회원가입 후 자동 로그인
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, signUp }}>
      {children}
    </AuthContext.Provider>
  );
}
