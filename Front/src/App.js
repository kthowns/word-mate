import React, { useContext, useState, useEffect } from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  Link,
  useNavigate,
  useLocation,
} from 'react-router-dom';
import { AuthProvider, AuthContext } from './AuthContext';
import './App.css';
import logo from './logo.png';
import Flashcard from './flashcard.js';
import OXQuiz from './OXQuiz.js'; // 정확한 대소문자로 수정
import FillIn from './fillin.js';
import LoginForm from './LoginForm';
import SignUpForm from './SignUpForm';
import Modal from './Modal';

function App() {
  const [vocabularies, setVocabularies] = useState(() => {
    const savedVocabularies = localStorage.getItem('vocabularies');
    return savedVocabularies ? JSON.parse(savedVocabularies) : [];
  });
  const [showAddModal, setShowAddModal] = useState(false);
  const [newVocab, setNewVocab] = useState({ title: '', description: '' });

  const openAddModal = () => setShowAddModal(true);
  const closeAddModal = () => setShowAddModal(false);

  const handleSubmitVocabulary = () => {
    if (newVocab.title) {
      setVocabularies([...vocabularies, { ...newVocab, id: Date.now(), wordCount: 0 }]);
      setNewVocab({ title: '', description: '' });
      closeAddModal();
    } else {
      alert('제목을 입력해 주세요.');
    }
  };

  const deleteVocabulary = (id) => {
    setVocabularies(vocabularies.filter((vocab) => vocab.id !== id));
  };

  useEffect(() => {
    localStorage.setItem('vocabularies', JSON.stringify(vocabularies));
  }, [vocabularies]);

  return (
    <AuthProvider>
      <Router>
        <div className="container">
          <Header openAddModal={openAddModal} />
          <Routes>
            <Route
              path="/"
              element={
                <Home
                  vocabularies={vocabularies}
                  deleteVocabulary={deleteVocabulary}
                />
              }
            />
            <Route path="/login" element={<LoginFormWrapper />} />
            <Route path="/signup" element={<SignUpFormWrapper />} />
            <Route path="/flashcard" element={<PrivateRoute component={Flashcard} />} />
            <Route path="/oxquiz" element={<PrivateRoute component={OXQuiz} />} />
            <Route path="/fillin" element={<PrivateRoute component={FillIn} />} />
          </Routes>
          {showAddModal && (
            <Modal title="단어장 추가" onClose={closeAddModal}>
              <label>제목:</label>
              <input
                type="text"
                value={newVocab.title}
                onChange={(e) => setNewVocab({ ...newVocab, title: e.target.value })}
                className="input-field"
              />
              <label>설명:</label>
              <textarea
                value={newVocab.description}
                onChange={(e) => setNewVocab({ ...newVocab, description: e.target.value })}
                className="textarea-field"
              />
              <button onClick={handleSubmitVocabulary} className="submit-button">
                제출
              </button>
            </Modal>
          )}
        </div>
      </Router>
    </AuthProvider>
  );
}

function Header({ openAddModal }) {
  const { user, logout } = useContext(AuthContext);

  return (
    <header>
      <div className="header-left">
        <img src={logo} alt="Logo" className="logo" />
        <button className="add-button" onClick={openAddModal}>
          +
        </button>
      </div>
      <div className="user-info">
        {user ? (
          <>
            <span>{user.email}님 환영합니다!</span>
            <button onClick={logout} className="logout-button">
              로그아웃
            </button>
          </>
        ) : (
          <>
            <button className="login-button">
              <Link to="/login">로그인</Link>
            </button>
            <button className="signup-button">
              <Link to="/signup">회원가입</Link>
            </button>
          </>
        )}
      </div>
    </header>
  );
}

function Home({ vocabularies, deleteVocabulary }) {
  return (
    <main>
      <h1>단어장 앱</h1>
      <section className="vocab-list">
        {vocabularies.length > 0 ? (
          vocabularies.map((vocab) => (
            <div key={vocab.id} className="vocab-item">
              <h2>
                {vocab.title} ({vocab.wordCount} 단어)
              </h2>
              <p className="description">{vocab.description}</p>
              <div className="vocab-buttons-container">
                <div className="vocab-buttons">
                  <button className="vocab-button">
                    <Link to="/flashcard">플래시 카드</Link>
                  </button>
                  <button className="vocab-button">
                    <Link to="/oxquiz">O/X</Link>
                  </button>
                  <button className="vocab-button">
                    <Link to="/fillin">빈칸 채우기</Link>
                  </button>
                </div>
                <div className="action-buttons">
                  <button
                    className="vocab-button delete-button"
                    onClick={() => deleteVocabulary(vocab.id)}
                  >
                    삭제
                  </button>
                </div>
              </div>
            </div>
          ))
        ) : (
          <p>단어장이 없습니다. 단어장을 추가해보세요!</p>
        )}
      </section>
    </main>
  );
}

function SignUpFormWrapper() {
  const { signUp } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSignUp = async (email, password) => {
    try {
      await signUp(email, password);
      alert('회원가입 성공! 자동으로 로그인되었습니다.');
      navigate('/'); // 홈 화면으로 이동
    } catch (err) {
      alert('회원가입 실패: ' + err.message);
    }
  };

  return <SignUpForm onSignUp={handleSignUp} />;
}

function LoginFormWrapper() {
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || '/';

  const handleLogin = async (email, password) => {
    try {
      await login(email, password);
      alert('로그인 성공!');
      navigate(from);
    } catch (err) {
      alert('로그인 실패: ' + err.message);
    }
  };

  return <LoginForm onLogin={handleLogin} />;
}

function PrivateRoute({ component: Component }) {
  const { user } = useContext(AuthContext);
  const location = useLocation();

  if (!user) {
    return <Navigate to="/login" state={{ from: location }} />;
  }

  return <Component />;
}

export default App;
