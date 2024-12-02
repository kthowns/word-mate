import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';

function randomInt(min, max, count) {
  if (max - min + 1 < count) {
    throw new Error("The range is too small for the number of unique values requested.");
  }

  const numbers = Array.from({ length: max - min + 1 }, (_, i) => i + min); // [min, ..., max]

  // 섞기
  for (let i = numbers.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [numbers[i], numbers[j]] = [numbers[j], numbers[i]]; // Swap
  }

  return numbers.slice(0, count); // 요청된 count 길이만큼 반환
}

const OXQuiz = () => {
  const { id } = useParams();
  const [totalQuestions, setTotalQuestions] = useState(0);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [wrongCount, setWrongCount] = useState(0);
  const [corCount, setCorCount] = useState(0);
  const [result, setResult] = useState('');
  const [words, setWords] = useState([]); // 단어 목록
  const [wordsForQuiz, setWordsForQuiz] = useState([]); // 퀴즈에 쓰일 단어 목록
  const [isQuizEnd, setIsQuizEnd] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isButtonDisabled, setIsButtonDisabled] = useState(false);

  const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

  const fetchVocabData = async () => {
    try {
      setIsLoading(true);
      setCurrentQuestion(0);
      setWrongCount(0);
      setCorCount(0);
      setResult('');
      setWords([]);
      setWordsForQuiz([]);
      setIsQuizEnd(false);
      setIsLoading(true);
      setIsButtonDisabled(false);

      const wordResponse = await fetch(`/api/words/all?vocab_id=${id}`);
      const wordData = await wordResponse.json();
      if (wordData.status === 200) {
        const wordsWithDefs = await Promise.all(wordData.data.map(async (word) => {
          const defsData = await fetch(`/api/defs/all?word_id=${word.wordId}`)
              .then((res) => res.json())
              .then((data) => (data.status === 200 ? data.data : []))
              .catch((err) => {
                console.error("뜻 정보 불러오기 실패", err);
                return [];
              });

          const statsData = await fetch(`/api/stats/detail?word_id=${word.wordId}`)
              .then((res) => res.json())
              .then((data) => (data.status === 200 ? data.data : []))
              .catch((err) => {
                console.error("통계 정보 불러오기 실패", err);
                return [];
              });

          return { ...word, defs: defsData, stats: statsData };
        }));

        if(wordsWithDefs.length == 0){ //빈 단어장
          setResult("단어가 없습니다.");
          setWordsForQuiz([{expression: " ", definition: " "}]);
          setIsLoading(false);
          setIsQuizEnd(true);
          return;
        }

        const wordsWithRandomDefs = [];
        const randNums = randomInt(0, wordsWithDefs.length-1, wordsWithDefs.length);
        console.log("randNums : ", randNums);
        wordsWithDefs.forEach((word, index, array) => {
          const randWordNum = randNums[index];
          const randDefNum = 0;
          randomInt(0, array[randWordNum].defs.length - 1, 1)[0];
          console.log("ran num", randWordNum, randDefNum);
          let def = "";
          if(array[randWordNum].defs.length > 0){
            console.log("defs Length:",array[randWordNum].defs.length);
            def = array[randWordNum].defs[randDefNum].definition;
          }
          const tuple = {expression: word.expression, definition: def};
          console.log("tuple", tuple);
          wordsWithRandomDefs.push(tuple);
        });
        setWords(wordsWithDefs);
        setTotalQuestions(wordsWithDefs.length);
        setWordsForQuiz(wordsWithRandomDefs);
        console.log("wordsWithRandomDefs", wordsWithRandomDefs);
      } else {
        console.error("단어 정보 불러오기 실패");
      }

      setIsLoading(false);
    } catch (error) {
      console.error('Error fetching vocab data:', error);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    console.log("words", words);
    console.log("wordsForQuiz", wordsForQuiz);
  }, [words, wordsForQuiz]);

  useEffect(() => {
    fetchVocabData();
  }, [id]);

  const checkAnswer = async (input) => {
    setIsButtonDisabled(true);

    const isSame = () => {
      const result = words[currentQuestion].defs.find(
          (def) => def.definition === wordsForQuiz[currentQuestion].definition);
      return result ? true : false;
    }

    words[currentQuestion].expression === wordsForQuiz[currentQuestion].expression;
    let correct = corCount;
    let wrong = wrongCount;

    if (input == isSame()) {
      correct += 1;
      setCorCount(correct);
      setResult("정답입니다!");
      words[currentQuestion].stats.correctCount += 1;
    } else {
      wrong += 1;
      setWrongCount(wrong);
      setResult("틀렸습니다.");
      words[currentQuestion].stats.incorrectCount += 1;
    }

    await delay(2000);

    if (currentQuestion + 1 < totalQuestions) {
      setCurrentQuestion((prevQuestion) => prevQuestion + 1);
      setResult('');
      setIsButtonDisabled(false);
    } else {
      setIsQuizEnd(true);
      setResult(`퀴즈가 끝났습니다.\n맞힌 횟수: ${correct}\n틀린 횟수: ${wrong}`);
      try {
        // 모든 데이터를 순차적으로 POST
        const results = await Promise.all(
            words.map(async (word) => {
              const response = await fetch('/api/stats/'+word.wordId, {
                method: 'PATCH',
                headers: {
                  'Content-Type': 'application/json',
                },
                body: JSON.stringify(word.stats),
              });

              if (!response.ok) {
                console.error(`데이터 전송 실패: ${word}`, await response.text());
                return null;
              }

              return await response.json(); // 성공적으로 전송된 데이터의 응답
            })
        );

        console.log('전송 결과:', results);
        return results; // 전체 전송 결과 반환
      } catch (error) {
        console.error('전송 중 오류 발생:', error);
      }
    }
  };

  return (
      <div style={{
          fontFamily: 'TTHakgyoansimEunhasuR',
          backgroundColor: isDarkMode ? '#242526' : '#f8f9fa',
          display: 'flex',
          flexDirection: 'column',
          height: '100vh',
          position: 'relative',
          padding: '20px',
          color: isDarkMode ? '#e4e6eb' : '#000',
          borderRadius: '20px',
          boxShadow: isDarkMode 
              ? '0 0 20px rgba(0,0,0,0.3)' 
              : '0 0 20px rgba(0,0,0,0.1)',
      }}>
        <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid #ddd', padding: '10px', height: '10%' }}>
          <p>{"맞힌 문제 수: "+corCount}</p>
          <p>{currentQuestion+1+"/"+totalQuestions}</p>
          <p>{"틀린 문제 수: "+wrongCount}</p>
        </header>

        <main style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '75%' }}>
          {isLoading ? (
              <div style={{ fontSize: '24px', fontWeight: 'bold' }}>로딩 중...</div>
          ) : (
              <section style={{ backgroundColor: 'white', border: '1px solid #ccc', width: '60%', marginBottom: '20px', textAlign: 'center', padding: '20px', borderRadius: '8px' }}>
                <p>단어: {wordsForQuiz[currentQuestion].expression || '불러오는 중...'}</p>
                <p>뜻: {wordsForQuiz[currentQuestion].definition || '불러오는 중...'}</p>
                <p style={{ display: result ? 'block' : 'none', margin: '20px 0' }}><pre>{result}</pre></p>
              </section>
          )}
          <div style={{ display: 'flex', gap: '10px' }}>
            <button onClick={() => checkAnswer(true)} style={{ borderRadius: '50px', width: '100px', height: '100px' }} disabled={isQuizEnd || isLoading || isButtonDisabled}>O</button>
            <button onClick={() => checkAnswer(false)} style={{ borderRadius: '50px', width: '100px', height: '100px' }} disabled={isQuizEnd || isLoading || isButtonDisabled}>X</button>
          </div>
        </main>
      </div>
  );
};

export default OXQuiz;
