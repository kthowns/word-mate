import React, { useState } from 'react';

const OXQuiz = ({ vocabularies, onUpdateVocabulary, isDarkMode }) => {
  const [totalQuestions, setTotalQuestions] = useState(0);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [wrongCount, setWrongCount] = useState(0);
  const [corCount, setCorCount] = useState(0);
  const [result, setResult] = useState('');
  const [selectedVocab, setSelectedVocab] = useState('');

  const updateHeader = () => {
    return {
      questionCount: `맞힌 문제 수: ${corCount}`,
      progress: `진행률: ${currentQuestion}/${totalQuestions}`,
      wrongCount: `틀린 문제 수: ${wrongCount}`,
    };
  };

  const checkAnswer = (isCorrect) => {
    if (isCorrect) {
      setCorCount(corCount + 1);
      setResult("정답입니다!");
    } else {
      setWrongCount(wrongCount + 1);
      setResult("틀렸습니다.");
    }
  };

  const addVocab = (event) => {
    const selected = event.target.value;
    if (selected) {
      setSelectedVocab('');
      // 선택한 단어장에 단어 추가 로직이 여기에 필요
    }
  };

  const headerData = updateHeader();

  // 버튼 스타일
  const buttonStyle = {
    padding: '8px 16px',
    backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
    color: isDarkMode ? '#e4e6eb' : '#000',
    border: isDarkMode ? '1px solid #a9c6f8' : '1px solid #ddd',
    borderRadius: '4px',
    cursor: 'pointer',
    transition: 'all 0.2s ease',
  };

  // O/X 버튼 스타일
  const oxButtonStyle = {
    borderRadius: '50px', 
    width: '100px', 
    height: '100px',
    backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
    color: isDarkMode ? '#e4e6eb' : '#fff',
    fontSize: '24px',
    cursor: 'pointer',
    transition: 'all 0.3s ease',
    boxShadow: isDarkMode ? 'none' : '0 4px 8px rgba(0,0,0,0.1)'
  };

  // 콤보박스 스타일
  const selectStyle = {
    backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
    color: isDarkMode ? '#e4e6eb' : '#000',
    border: `1px solid ${isDarkMode ? '#3a3b3c' : '#ddd'}`,
    padding: '5px',
    borderRadius: '4px',
    cursor: 'pointer',
    transition: 'all 0.2s ease',
  };

  return (
    <div className={`${isDarkMode ? 'dark-mode' : ''}`} style={{ 
      fontFamily: 'TTHakgyoansimEunhasuR',
      backgroundColor: isDarkMode ? '#242526' : '#f8f9fa',
      display: 'flex', 
      flexDirection: 'column', 
      height: '100vh',
      position: 'relative',
      padding: '20px',
      color: isDarkMode ? '#e4e6eb' : '#000'
    }}>
      <header style={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center', 
        borderBottom: `2px solid ${isDarkMode ? '#404040' : '#ddd'}`,
        padding: '10px', 
        height: '10%'
      }}>
        <p style={{ fontSize: '25px' }}>{headerData.wrongCount}</p>
        <p style={{ fontSize: '25px' }}>{headerData.progress}</p>
        <p style={{ fontSize: '25px' }}>{headerData.questionCount}</p>
      </header>

      <main style={{
        flex: 1,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        height: '75%'
      }}>
        <section style={{
          backgroundColor: isDarkMode ? '#3a3b3c' : 'white',
          border: `1px solid ${isDarkMode ? '#404040' : '#ccc'}`,
          width: '60%',
          padding: '20px',
          borderRadius: '8px',
          textAlign: 'center',
          marginBottom: '20px'
        }}>
          <p>단어: </p>
          <p>뜻: </p>
          <p style={{ 
            display: result ? 'block' : 'none', 
            margin: '20px 0',
            color: isDarkMode ? '#e4e6eb' : '#000' 
          }}>
            {`정답 여부: ${result}`}
          </p>
        </section>
        <div style={{ display: 'flex', gap: '10px' }}>
          <button 
            onClick={() => checkAnswer(true)} 
            style={{
              ...oxButtonStyle,
              backgroundColor: isDarkMode ? '#3a3b3c' : '#f0bbf8',
              border: isDarkMode ? '2px solid #a9c6f8' : 'none',
            }}
          >O</button>
          <button 
            onClick={() => checkAnswer(false)} 
            style={{
              ...oxButtonStyle,
              backgroundColor: isDarkMode ? '#3a3b3c' : '#a9c6f8',
              border: isDarkMode ? '2px solid #f0bbf8' : 'none',
            }}
          >X</button>
        </div>
      </main>

      <footer style={{ 
        display: 'flex', 
        justifyContent: 'space-around', 
        alignItems: 'center', 
        borderTop: `2px solid ${isDarkMode ? '#404040' : '#ddd'}`, 
        padding: '10px 20px',
        height: '15%'
      }}>
        <button style={buttonStyle}>이전</button>
        <select 
          value={selectedVocab} 
          onChange={addVocab}
          style={selectStyle}
        >
          <option value="">단어장에 추가</option>
          <option value="단어장1">단어장1</option>
          <option value="단어장2">단어장2</option>
          <option value="단어장3">단어장3</option>
        </select>
        <button style={buttonStyle}>다음</button>
      </footer>
    </div>
  );
};

export default OXQuiz;
