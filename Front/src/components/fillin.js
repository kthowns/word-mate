import React, { useState } from 'react';

const FillInTheBlank = ({ vocabularies, onUpdateVocabulary, isDarkMode }) => {
  const [totalQuestions, setTotalQuestions] = useState(0);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [wrongCount, setWrongCount] = useState(0);
  const [corCount, setCorCount] = useState(0);
  const [userInput, setUserInput] = useState('');
  const [result, setResult] = useState('');
  const [selectedVocab, setSelectedVocab] = useState('');

  const correctAnswer = "정답";

  const updateHeader = () => {
    return {
      questionCount: `맞힌 문제 수: ${corCount}`,
      progress: `진행률: ${currentQuestion}/${totalQuestions}`,
      wrongCount: `틀린 문제 수: ${wrongCount}`,
    };
  };

  const checkAnswer = () => {
    if (userInput === correctAnswer) {
      setResult("정답입니다!");
      setCorCount(corCount + 1);
    } else {
      setResult("틀렸습니다.");
      setWrongCount(wrongCount + 1);
    }
  };

  const addVocab = (event) => {
    const selected = event.target.value;
    if (selected) {
      setSelectedVocab('');
      // 추가 로직이 여기에 필요
    }
  };

  const headerData = updateHeader();

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
            height: '10%',
            color: isDarkMode ? '#e4e6eb' : '#000'
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
                backgroundColor: isDarkMode ? '#2d2d2d' : 'white',
                border: `1px solid ${isDarkMode ? '#404040' : '#ccc'}`,
                width: '60%',
                padding: '20px',
                borderRadius: '8px',
                textAlign: 'center',
                marginBottom: '20px'
            }}>
                <p>문제: <span>___</span></p>
            </section>
            <section style={{ width: '60%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                <input
                    type="text"
                    value={userInput}
                    onChange={(e) => setUserInput(e.target.value)}
                    placeholder="답 입력"
                    style={{ 
                        width: '60%', 
                        height: '30px', 
                        marginRight: '10px',
                        backgroundColor: isDarkMode ? '#5a5b5d' : '#fff',
                        color: isDarkMode ? '#e4e6eb' : '#000',
                        border: `2px solid ${isDarkMode ? '#3a3b3c' : '#ddd'}`,
                        borderRadius: '4px',
                        padding: '0 10px'
                    }}
                />
                <button 
                    onClick={checkAnswer}
                    style={{
                        padding: '8px 16px',
                        backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
                        color: isDarkMode ? '#e4e6eb' : '#000',
                        border: isDarkMode ? '1px solid #a9c6f8' : '2px solid #ddd',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    제출
                </button>
            </section>
            {result && <p style={{ margin: '20px 0' }}>{result}</p>}
        </main>

      <footer style={{ 
        display: 'flex', 
        justifyContent: 'space-around', 
        alignItems: 'center', 
        borderTop: `2px solid ${isDarkMode ? '#404040' : '#ddd'}`,
        padding: '10px 20px',
        height: '15%'
    }}>
        <button style={{
            padding: '8px 16px',
            backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
            color: isDarkMode ? '#e4e6eb' : '#000',
            border: isDarkMode ? '1px solid #a9c6f8' : '2px solid #ddd',
            borderRadius: '4px',
            cursor: 'pointer',
            transition: 'all 0.2s ease',
        }}>이전</button>
        <select 
            value={selectedVocab} 
            onChange={addVocab}
            style={{
                backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
                color: isDarkMode ? '#e4e6eb' : '#000',
                border: `1px solid ${isDarkMode ? '#3a3b3c' : '#ddd'}`,
                padding: '5px',
                borderRadius: '4px'
            }}
        >
            <option value="">단어장에 추가</option>
            <option value="단어장1">단어장1</option>
            <option value="단어장2">단어장2</option>
            <option value="단어장3">단어장3</option>
        </select>
        <button style={{
            padding: '8px 16px',
            backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
            color: isDarkMode ? '#e4e6eb' : '#000',
            border: isDarkMode ? '1px solid #a9c6f8' : '2px solid #ddd',
            borderRadius: '4px',
            cursor: 'pointer',
            transition: 'all 0.2s ease',
        }}>다음</button>
      </footer>
    </div>
  );
};

export default FillInTheBlank;
