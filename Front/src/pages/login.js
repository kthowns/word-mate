// Login.js
import React, { useState } from 'react';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    if (!username || !password) {
      alert('아이디와 비밀번호를 모두 입력해주세요.');
    }
  };

  return (
    <div style={styles.container}>
      <header style={styles.header}>로그인</header>
      <main style={styles.main}>
        <input
          type="text"
          placeholder="아이디 입력"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={styles.input}
          required
        />
        <input
          type="password"
          placeholder="비밀번호 입력"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          style={styles.input}
          required
        />
        <button onClick={handleLogin} style={styles.button}>로그인</button>
      </main>
    </div>
  );
};

const styles = {
  container: {
    margin: 0,
    padding: 0,
    backgroundColor: '#f8f9fa',
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    fontSize: '36px',
    marginBottom: '30px',
    color: '#333',
  },
  main: {
    backgroundColor: 'white',
    border: '1px solid #ccc',
    borderRadius: '8px',
    padding: '40px',
    width: '400px',
    textAlign: 'center',
  },
  input: {
    width: '100%',
    height: '40px',
    marginBottom: '15px',
    padding: '10px',
    border: '1px solid #ccc',
    borderRadius: '5px',
    fontSize: '16px',
  },
  button: {
    width: '100%',
    height: '45px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    fontSize: '18px',
  },
  buttonHover: {
    backgroundColor: '#0056b3',
  },
};

export default Login;
