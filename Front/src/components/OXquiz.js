import React, { useState } from 'react';

const OXQuiz = () => {
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

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', backgroundColor: '#f8f9fa' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid #ddd', padding: '10px', height: '10%' }}>
        <p>{headerData.wrongCount}</p>
        <p>{headerData.progress}</p>
        <p>{headerData.questionCount}</p>
      </header>

      <main style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '75%' }}>
        <section style={{ backgroundColor: 'white', border: '1px solid #ccc', width: '60%', marginBottom: '20px', textAlign: 'center', padding: '20px', borderRadius: '8px' }}>
          <p>단어: </p>
          <p>뜻: </p>
          <p style={{ display: result ? 'block' : 'none', margin: '20px 0' }}>{`정답 여부: ${result}`}</p>
        </section>
        <div style={{ display: 'flex', gap: '10px' }}>
          <button onClick={() => checkAnswer(true)} style={{ borderRadius: '50px', width: '100px', height: '100px' }}>O</button>
          <button onClick={() => checkAnswer(false)} style={{ borderRadius: '50px', width: '100px', height: '100px' }}>X</button>
        </div>
      </main>

      <footer style={{ display: 'flex', justifyContent: 'space-around', alignItems: 'center', borderTop: '2px solid #ddd', height: '15%' }}>
        <button>이전</button>
        <select value={selectedVocab} onChange={addVocab}>
          <option value="">단어장에 추가</option>
          <option value="단어장1">단어장1</option>
          <option value="단어장2">단어장2</option>
          <option value="단어장3">단어장3</option>
        </select>
        <button>다음</button>
      </footer>
    </div>
  );
};

export default OXQuiz;
