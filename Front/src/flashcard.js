// Flashcard.js
import React, { useState, useEffect } from 'react';

const Flashcard = () => {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [totalQuestions, setTotalQuestions] = useState(0);
  const [isMeaningVisible, setIsMeaningVisible] = useState(false);
  const [selectedVocab, setSelectedVocab] = useState('');

  useEffect(() => {
    updateHeader();
  }, [currentQuestion, totalQuestions]);

  const updateHeader = () => {
    return `진행률: ${currentQuestion}/${totalQuestions}`;
  };

  const toggleFlashcard = () => {
    setIsMeaningVisible(!isMeaningVisible);
  };

  const playAudio = (event) => {
    event.stopPropagation();
    // 음성을 재생하는 코드 추가
  };

  const addVocab = () => {
    if (selectedVocab) {
      // 선택한 단어장에 단어 추가 로직
      setSelectedVocab('');
    }
  };

  return (
    <div style={styles.container}>
      <header style={styles.header}>
        <p>{updateHeader()}</p>
      </header>
      
      <main style={styles.main}>
        <div className="flashcard" onClick={toggleFlashcard} style={styles.flashcard}>
          <div className="word" style={{ display: isMeaningVisible ? 'none' : 'flex' }}>
            <p>단어: apple</p>
            <button id="audio-button" onClick={playAudio} style={styles.audioButton}>듣기</button>
          </div>
          <div className="meaning" style={{ display: isMeaningVisible ? 'block' : 'none' }}>
            <p>뜻: 사과</p>
            <p>품사: 명사</p>
          </div>
        </div>
      </main>

      <footer style={styles.footer}>
        <button onClick={() => setCurrentQuestion(currentQuestion - 1)}>이전</button>
        <select
          value={selectedVocab}
          onChange={(e) => setSelectedVocab(e.target.value)}
          onBlur={addVocab}
          style={styles.select}
        >
          <option value="">단어장에 추가</option>
          <option value="단어장1">단어장1</option>
          <option value="단어장2">단어장2</option>
          <option value="단어장3">단어장3</option>
        </select>
        <button onClick={() => setCurrentQuestion(currentQuestion + 1)}>다음</button>
      </footer>
    </div>
  );
};

const styles = {
  container: {
    backgroundColor: '#f8f9fa',
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
  },
  header: {
    height: '10%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderBottom: '2px solid #ddd',
    backgroundColor: '#ffffff',
  },
  main: {
    height: '75%',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  },
  flashcard: {
    width: '500px',
    height: '300px',
    backgroundColor: '#ffffff',
    border: '1px solid #ccc',
    borderRadius: '10px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    fontSize: '24px',
    textAlign: 'center',
    position: 'relative',
  },
  audioButton: {
    position: 'absolute',
    top: '10px',
    left: '10px',
    borderRadius: '50%',
    padding: '5px 10px',
  },
  footer: {
    height: '15%',
    display: 'flex',
    justifyContent: 'space-around',
    alignItems: 'center',
    borderTop: '2px solid #ddd',
    backgroundColor: '#ffffff',
  },
  select: {
    padding: '10px',
    borderRadius: '5px',
  },
};

export default Flashcard;
