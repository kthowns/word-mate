// App.js
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Modal from './Modal';
import './App.css';
import logo from './logo.png';
import Flashcard from './Flashcard'; // Flashcard 컴포넌트
import OXQuiz from './OXQuiz'; // OXQuiz 컴포넌트
import FillIn from './FillIn'; // FillIn 컴포넌트

function App() {
  const [vocabularies, setVocabularies] = useState(() => {
    const savedVocabularies = localStorage.getItem('vocabularies');
    return savedVocabularies ? JSON.parse(savedVocabularies) : [];
  });
  const [showAddModal, setShowAddModal] = useState(false);
  const [showUIModal, setShowUIModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [newVocab, setNewVocab] = useState({ title: '', description: '' });
  const [editingVocab, setEditingVocab] = useState(null); // 수정 중인 단어장 추적

  useEffect(() => {
    localStorage.setItem('vocabularies', JSON.stringify(vocabularies));
  }, [vocabularies]);

  const openAddModal = () => setShowAddModal(true);
  const closeAddModal = () => setShowAddModal(false);

  const openUIModal = () => setShowUIModal(true);
  const closeUIModal = () => setShowUIModal(false);

  const openEditModal = (vocab) => {
    setEditingVocab(vocab);
    setNewVocab({ title: vocab.title, description: vocab.description });
    setShowEditModal(true);
  };
  const closeEditModal = () => {
    setEditingVocab(null);
    setShowEditModal(false);
  };

  const handleSubmitVocabulary = () => {
    if (newVocab.title) {
      setVocabularies([...vocabularies, { ...newVocab, id: Date.now(), wordCount: 0 }]);
      setNewVocab({ title: '', description: '' });
      closeAddModal();
    } else {
      alert('제목을 입력해 주세요.');
    }
  };

  const handleUpdateVocabulary = () => {
    if (editingVocab) {
      setVocabularies(
        vocabularies.map((vocab) =>
          vocab.id === editingVocab.id ? { ...vocab, ...newVocab } : vocab
        )
      );
      closeEditModal();
    }
  };

  const deleteVocabulary = (id) => {
    setVocabularies(vocabularies.filter((vocab) => vocab.id !== id));
  };

  return (
    <Router>
      <div className="container">
        <header>
          <div className="header-left">
            <img src={logo} alt="Logo" className="logo" />
            <button className="add-button" onClick={openAddModal}>+</button>
            <button className="add-button" onClick={openUIModal}>UI 설정</button>
          </div>
          <div className="learning-rate">
            <p>학습률 통계 섹션</p>
          </div>
        </header>

        <main>
          <section className="vocab-list">
            {vocabularies.map((vocab) => (
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
                      className="vocab-button edit-button"
                      onClick={() => openEditModal(vocab)}
                    >
                      수정
                    </button>
                    <button
                      className="vocab-button delete-button"
                      onClick={() => deleteVocabulary(vocab.id)}
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </section>
        </main>

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

        {showEditModal && (
          <Modal title="단어장 수정" onClose={closeEditModal}>
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
            <button onClick={handleUpdateVocabulary} className="submit-button">
              수정
            </button>
          </Modal>
        )}

        {showUIModal && (
          <Modal title="UI 설정" onClose={closeUIModal}>
            <p>UI 설정 요소를 추가할 공간</p>
            <button onClick={closeUIModal} className="submit-button">
              완료
            </button>
          </Modal>
        )}
      </div>

      <Routes>
        <Route path="/flashcard" element={<Flashcard />} />
        <Route path="/oxquiz" element={<OXQuiz />} />
        <Route path="/fillin" element={<FillIn />} />
      </Routes>
    </Router>
  );
}

export default App;
