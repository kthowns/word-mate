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

async function fetchJson(url, method = 'GET', body = null) {
  const headers = { 'Content-Type': 'application/json' };
  const options = { method, headers };

  if (body) {
    options.body = JSON.stringify(body);
  }

  const response = await fetch(url, options);
  const data = await response.json();
  if (data.status === 200) {
    return data;
  } else {
    console.error("Failed to load " + url);
    return [];
  }
}

function App(uId) {
  const [vocabularies, setVocabularies] = useState([]);
  const [userId, setUserId] = useState(1);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [newVocab, setNewVocab] = useState({ title: '', description: '' });
  const [editingVocab, setEditingVocab] = useState(null);
  const [isDarkMode, setIsDarkMode] = useState(() => {
    const savedMode = localStorage.getItem('darkMode');
    return savedMode ? JSON.parse(savedMode) : false;
  });
  const [expandedVocabId, setExpandedVocabId] = useState(null);
  const [currentView, setCurrentView] = useState(null); // 'vocabulary', 'flashcard', 'oxquiz', 'fillin'

  const fetchVocabData = async () => {
    try {
      const vocabData = await fetchJson(`/api/vocabs/all?user_id=${userId}`);
      if (vocabData.status === 200) {
        const vocabsWithStats = await Promise.all(
            vocabData.data.map(async (vocab) => {
              const statData = await fetch(`/api/stats/all?vocab_id=${vocab.vocabId}`)
                  .then((res) => res.json())
                  .then((data) => (data.status === 200 ? data.data : []))
                  .catch((err) => {
                    console.error("통계 정보 불러오기 실패", err);
                    return [];
                  });

              return { ...vocab, stats: statData };
            })
        );
        setVocabularies(vocabsWithStats);
      } else {
        console.error("단어장 정보 불러오기 실패");
        setVocabularies([]);
      }
    } catch (error) {
      console.error('Error fetching vocab data:', error);
    }
  };

  useEffect(() => {
    fetchVocabData();
  }, [userId]);

  useEffect(() => {
    console.log(vocabularies);
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

  const handleSubmitVocabulary = async () => {
    if (newVocab.title) {
      const vocabToAdd = { title: newVocab.title, description: newVocab.description };
      await fetchJson(`/api/vocabs/${userId}`, 'POST', vocabToAdd);
      await fetchVocabData();
      setNewVocab({ title: '', description: '' });
      closeAddModal();
    } else {
      alert('제목을 입력해 주세요.');
    }
  };

  const handleUpdateVocabulary = async () => {
    if (editingVocab) {
      const updatedVocab = { title: newVocab.title, description: newVocab.description };
      await fetchJson(`/api/vocabs/${editingVocab.vocabId}`, 'PATCH', updatedVocab);
      await fetchVocabData();
    }
    closeEditModal();
  };

  const deleteVocabulary = async (vocabId) => {
    try{
      const deleteResponse = await fetchJson(`/api/vocabs/${vocabId}`, 'DELETE');
      if(deleteResponse.status === 200)
        console.log("단어장 삭제 완료", deleteResponse.data);
      else
        console.error("단어장 삭제 실패");
    }catch(e){
      console.error("단어장 삭제 실패", e);
      return;
    }
    await fetchVocabData();
  };

  const toggleVocabExpand = (vocabId, view = null) => {
    if (expandedVocabId === vocabId && currentView === view) {
        setExpandedVocabId(null);
        setCurrentView(null);
    } else {
        setExpandedVocabId(vocabId);
        setCurrentView(view);
    }
  };

  const returnToVocabList = (vocabId) => {
    setExpandedVocabId(vocabId);
    setCurrentView(null);
  };

  const renderVocabContent = (vocab) => {
    if (expandedVocabId !== vocab.vocabId) return null;

    switch(currentView) {
      case 'flashcard':
        return <Flashcard
            isDarkMode={isDarkMode}
            vocabId={vocab.vocabId}
        />;
      case 'oxquiz':
        return <OXQuiz vocabularies={vocabularies} isDarkMode={isDarkMode} vocabId={vocab.vocabId} />;
      case 'fillin':
        return <FillIn vocabularies={vocabularies} isDarkMode={isDarkMode} vocabId={vocab.vocabId} />;
      default:
        return (
          <Vocabulary 
            vocabularies={vocabularies}
            isDarkMode={isDarkMode}
            vocabData={vocab}
            vocabId={vocab.vocabId}
          />
        );
    }
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
              <div key={vocab.vocabId} className="vocab-item">
                <div 
                  className="vocab-header"
                  onClick={() => toggleVocabExpand(vocab.vocabId)}
                >
                  <h2>{vocab.title} ({vocab.wordCount} 단어)</h2>
                  <p className="description">{vocab.description}</p>
                </div>
                <div className="vocab-buttons-container">
                  <div className="vocab-buttons">
                    <button 
                      className="vocab-button"
                      onClick={() => toggleVocabExpand(vocab.vocabId, 'flashcard')}
                    >
                      플래시 카드
                    </button>
                    <button 
                      className="vocab-button"
                      onClick={() => toggleVocabExpand(vocab.vocabId, 'oxquiz')}
                    >
                      O/X
                    </button>
                    <button 
                      className="vocab-button"
                      onClick={() => toggleVocabExpand(vocab.vocabId, 'fillin')}
                    >
                      빈칸 채우기
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
                      onClick={() => deleteVocabulary(vocab.vocabId)}
                    >
                      삭제
                    </button>
                  </div>
                </div>
                <div className="vocab-expand-section">
                  <button 
                    className="expand-button"
                    onClick={() => {
                        if (expandedVocabId === vocab.vocabId) {
                            setExpandedVocabId(null);
                            setCurrentView(null);
                        } else {
                            setExpandedVocabId(vocab.vocabId);
                            setCurrentView(null);
                        }
                    }}
                  >
                    {expandedVocabId === vocab.vocabId ? '단어목록 접기' : '단어목록 펼치기'}
                  </button>
                </div>
                {renderVocabContent(vocab)}
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
                path="/vocabulary/:vocabId"
                element={
                  <Vocabulary
                      isDarkMode={isDarkMode}
                      vocabId={expandedVocabId}
                  />
                }
            />
            <Route
                path="/flashcard/:vocabId"
                element={
                  <Flashcard
                      isDarkMode={isDarkMode}
                      vocabId={expandedVocabId}
                  />
                }
            />
            <Route
                path="/oxquiz/:vocabId"
                element={
                  <OXQuiz
                      isDarkMode={isDarkMode}
                      vocabId={expandedVocabId}
                  />
                }
            />
            <Route
                path="/fillin/:vocabId"
                element={
                  <FillIn
                      isDarkMode={isDarkMode}
                      vocabId={expandedVocabId}
                  />
                }
            />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
