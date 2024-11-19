import React, { useState } from 'react';

const FillInTheBlank = () => {
  const [totalQuestions, setTotalQuestions] = useState(0);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [wrongCount, setWrongCount] = useState(0);
  const [corCount, setCorCount] = useState(0);
  const [userInput, setUserInput] = useState('');
  const [result, setResult] = useState('');
  const [selectedVocab, setSelectedVocab] = useState('');

  const correctAnswer = "정답"; // 예시 정답

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
    <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', backgroundColor: '#f8f9fa' }}>
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid #ddd', padding: '10px', height: '10%' }}>
        <p>{headerData.wrongCount}</p>
        <p>{headerData.progress}</p>
        <p>{headerData.questionCount}</p>
      </header>

      <main style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center' }}>
        <section style={{ backgroundColor: 'white', border: '1px solid #ccc', width: '50%', padding: '20px', borderRadius: '8px', textAlign: 'center', marginBottom: '20px' }}>
          <p>문제: <span>___</span></p>
        </section>
        <section style={{ width: '60%', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <input
            type="text"
            value={userInput}
            onChange={(e) => setUserInput(e.target.value)}
            placeholder="답 입력"
            style={{ width: '60%', height: '30px', marginRight: '10px' }}
          />
          <button onClick={checkAnswer}>제출</button>
        </section>
        {result && <p style={{ margin: '20px 0' }}>{result}</p>}
      </main>

      <footer style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderTop: '2px solid #ddd', padding: '10px 20px' }}>
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

export default FillInTheBlank;
