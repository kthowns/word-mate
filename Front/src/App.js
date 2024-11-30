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
  const [showUIModal, setShowUIModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [newVocab, setNewVocab] = useState({ title: '', description: '' });
  const [editingVocab, setEditingVocab] = useState(null); 

  // API에서 데이터를 불러오는 useEffect
  useEffect(() => {
    const fetchVocabularies = async () => {
      try {
        const response = await fetch("http://127.0.0.1:8080/api/vocabs/all?user_id=1"); // 실제 API URL로 변경
        const data = await response.json();
        console.log(data);
        setVocabularies(data.data); // API로부터 받은 데이터를 상태에 저장
      } catch (error) {
        console.error('데이터를 불러오는 데 실패했습니다:', error);
      }
    };
    
    fetchVocabularies(); // 컴포넌트가 마운트될 때 API 호출
  }, []); // 빈 배열로, 컴포넌트 마운트 시 한 번만 실행되도록 설정

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
          vocab.vocabId === editingVocab.id ? { ...vocab, ...newVocab } : vocab
        )
      );
      closeEditModal();
    }
  };

  const deleteVocabulary = (id) => {
    setVocabularies(vocabularies.filter((vocab) => vocab.vocabId !== id));
  };

  const updateVocabularyWords = (vocabId, newWords) => {
    setVocabularies(vocabularies.map(vocab =>
        vocab.vocabId === vocabId
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
              <div key={vocab.vocabId} className="vocab-item">
                <Link to={`/vocabulary/${vocab.vocabId}`} className="vocab-title">
                  <h2>{vocab.title} ({vocab.wordCount} 단어)</h2>
                  <p className="description">{vocab.description}</p>
                </Link>
                <div className="vocab-buttons-container">
                  <div className="vocab-buttons">
                    <Link to={`/flashcard/${vocab.vocabId}`} className="vocab-button">
                      플래시 카드
                    </Link>
                    <Link to={`/oxquiz/${vocab.vocabId}`} className="vocab-button">
                      O/X
                    </Link>
                    <Link to={`/fillin/${vocab.vocabId}`} className="vocab-button">
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
                      onClick={() => deleteVocabulary(vocab.vocabId)}
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
              추가
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
        <Route 
          path="/vocabulary/:id" 
          element={
            <Vocabulary 
              vocabularies={vocabularies}
              onUpdateVocabulary={updateVocabularyWords}
            />
          } 
        />
        <Route 
          path="/flashcard/:id" 
          element={
            <Flashcard 
              vocabularies={vocabularies}
              onUpdateVocabulary={updateVocabularyWords}
            />
          } 
        />
        <Route 
          path="/oxquiz/:id" 
          element={
            <OXQuiz 
              vocabularies={vocabularies}
              onUpdateVocabulary={updateVocabularyWords}
            />
          } 
        />
        <Route 
          path="/fillin/:id" 
          element={
            <FillIn 
              vocabularies={vocabularies}
              onUpdateVocabulary={updateVocabularyWords}
            />
          } 
        />
      </Routes>
    </Router>
  );
}

export default App;
