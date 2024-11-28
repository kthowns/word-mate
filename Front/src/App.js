// App.js
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Modal from './components/Modal';
import './styles/App.css';
import logo from './resources/logo.png';
import Flashcard from './components/flashcard';
import OXQuiz from './components/OXquiz';
import FillIn from './components/fillin';
import Vocabulary from './components/vocabulary';

function App() {
  const [vocabularies, setVocabularies] = useState(() => {
    const savedVocabularies = localStorage.getItem('vocabularies');
    return savedVocabularies ? JSON.parse(savedVocabularies) : [];
  });
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [newVocab, setNewVocab] = useState({ title: '', description: '' });
  const [editingVocab, setEditingVocab] = useState(null);
  const [isDarkMode, setIsDarkMode] = useState(() => {
    const savedMode = localStorage.getItem('darkMode');
    return savedMode ? JSON.parse(savedMode) : false;
  });

  useEffect(() => {
    localStorage.setItem('vocabularies', JSON.stringify(vocabularies));
  }, [vocabularies]);

  useEffect(() => {
    localStorage.setItem('darkMode', JSON.stringify(isDarkMode));
    document.body.className = isDarkMode ? 'dark-mode' : '';
  }, [isDarkMode]);

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
  };

  const openAddModal = () => setShowAddModal(true);
  const closeAddModal = () => setShowAddModal(false);

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

  const updateVocabularyWords = (vocabId, newWords) => {
    setVocabularies(vocabularies.map(vocab => 
        vocab.id === vocabId 
            ? { 
                ...vocab, 
                words: newWords,
                wordCount: newWords.length 
              }
            : vocab
    ));
  };

  return (
    <Router>
      <div className={`container ${isDarkMode ? 'dark-mode' : ''}`}>
        <header>
          <div className="header-left">
            <img src={logo} alt="Logo" className="logo" />
            <button className="add-button" onClick={openAddModal}>Add</button>
            <button 
              className={`mode-toggle-button ${isDarkMode ? 'dark' : 'light'}`} 
              onClick={toggleDarkMode}
            >
              {isDarkMode ? 'NIGHT' : 'DAY'}
            </button>
          </div>
          <div className="learning-rate">
            <p>학습률 통계 섹션</p>
          </div>
        </header>

        <main>
          <section className="vocab-list">
            {vocabularies.map((vocab) => (
              <div key={vocab.id} className="vocab-item">
                <Link to={`/vocabulary/${vocab.id}`} className="vocab-title">
                  <h2>{vocab.title} ({vocab.wordCount} 단어)</h2>
                  <p className="description">{vocab.description}</p>
                </Link>
                <div className="vocab-buttons-container">
                  <div className="vocab-buttons">
                    <Link to={`/flashcard/${vocab.id}`} className="vocab-button">
                      플래시 카드
                    </Link>
                    <Link to={`/oxquiz/${vocab.id}`} className="vocab-button">
                      O/X
                    </Link>
                    <Link to={`/fillin/${vocab.id}`} className="vocab-button">
                      빈칸 채우기
                    </Link>
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

        <Routes>
          <Route 
            path="/vocabulary/:id" 
            element={
              <Vocabulary 
                vocabularies={vocabularies}
                onUpdateVocabulary={updateVocabularyWords}
                isDarkMode={isDarkMode}
              />
            } 
          />
          <Route 
            path="/flashcard/:id" 
            element={
              <Flashcard 
                vocabularies={vocabularies}
                onUpdateVocabulary={updateVocabularyWords}
                isDarkMode={isDarkMode}
              />
            } 
          />
          <Route 
            path="/oxquiz/:id" 
            element={
              <OXQuiz 
                vocabularies={vocabularies}
                onUpdateVocabulary={updateVocabularyWords}
                isDarkMode={isDarkMode}
              />
            } 
          />
          <Route 
            path="/fillin/:id" 
            element={
              <FillIn 
                vocabularies={vocabularies}
                onUpdateVocabulary={updateVocabularyWords}
                isDarkMode={isDarkMode}
              />
            } 
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
