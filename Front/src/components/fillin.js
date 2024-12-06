import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router-dom';

const FillInTheBlank = ({ isDarkMode, vocabId }) => {
    const [totalQuestions, setTotalQuestions] = useState(0);
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [wrongCount, setWrongCount] = useState(0);
    const [corCount, setCorCount] = useState(0);
    const [result, setResult] = useState('');
    const [words, setWords] = useState([]); // 단어 목록
    const [userInput, setUserInput] = useState('');
    const [isQuizEnd, setIsQuizEnd] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);

    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    const fetchVocabData = async () => {
        try {
            const wordResponse = await fetch(`/api/words/all?vocab_id=${vocabId}`);
            const wordData = await wordResponse.json();
            if (wordData.status === 200) {
                if (wordData.data.length === 0) { //빈 단어장
                    setResult("단어가 없습니다.");
                    setWords([]);
                    setIsLoading(false);
                    setIsQuizEnd(true);
                    return;
                }
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

                    return {...word, defs: defsData, stats: statsData};
                }));

                setWords(wordsWithDefs);
                setTotalQuestions(wordsWithDefs.length);
            } else {
                console.error("단어 정보 ���오기 실패");
            }

            setIsLoading(false);
        } catch (error) {
            console.error('Error fetching vocab data:', error);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        console.log("words", words);
    }, [words]);

    useEffect(() => {
        setIsLoading(true);
        setCurrentQuestion(0);
        setWrongCount(0);
        setCorCount(0);
        setResult('');
        setWords([]);
        setIsQuizEnd(false);
        setIsLoading(true);
        setIsButtonDisabled(false);
        setUserInput('');
        fetchVocabData();
    }, [vocabId]);

    const checkAnswer = async () => {
        setIsButtonDisabled(true);

        const isCorrect = () => {
            return userInput.toLowerCase() === words[currentQuestion].expression.toLowerCase();
        }

        let correct = corCount;
        let wrong = wrongCount;

        if (isCorrect()) {
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
        setUserInput('');

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
                        const response = await fetch('/api/stats/' + word.wordId, {
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

                console.log('전송 과:', results);
                return results; // 전체 전송 결과 반환
            } catch (error) {
                console.error('전송 중 오류 발생:', error);
            }
        }
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
            color: isDarkMode ? '#e4e6eb' : '#000',
            borderRadius: '20px',
            boxShadow: isDarkMode 
                ? '0 0 20px rgba(0,0,0,0.3)' 
                : '0 0 20px rgba(0,0,0,0.1)',
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
                <p>{"맞힌 문제 수: " + corCount}</p>
                <p>{currentQuestion + 1 + "/" + totalQuestions}</p>
                <p>{"틀린 문제 수: " + wrongCount}</p>
            </header>

            <main style={{
                flex: 1,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                height: '75%'
            }}>
                {words && words.length > 0 ? (
                <section style={{
                    backgroundColor: isDarkMode ? '#3a3b3c' : 'white',
                    border: `1px solid ${isDarkMode ? '#404040' : '#ddd'}`,
                    width: '60%',
                    padding: '20px',
                    borderRadius: '8px',
                    textAlign: 'center',
                    marginBottom: '20px',
                    boxShadow: isDarkMode 
                        ? '0 2px 4px rgba(0,0,0,0.2)' 
                        : '0 2px 4px rgba(0,0,0,0.1)'
                }}>
                    <p>문제: {words[currentQuestion]?.defs[0]?.definition || '불러오는 중...'}</p>
                </section>):("로딩 중...")}
                <section style={{
                    width: '60%', 
                    display: 'flex', 
                    justifyContent: 'center', 
                    alignItems: 'center',
                    gap: '10px'  // 간격 추가
                }}>
                    <input
                        type="text"
                        value={userInput}
                        onChange={(e) => setUserInput(e.target.value)}
                        placeholder="답 입력"
                        style={{
                            flex: '1',  // 변경: width 대신 flex 사용
                            height: '40px',  // 높이 조정
                            backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
                            color: isDarkMode ? '#e4e6eb' : '#000',
                            border: isDarkMode ? '2px solid #a9c6f8' : '1px solid #ddd',
                            borderRadius: '4px',
                            padding: '0 15px',
                            fontFamily: 'TTHakgyoansimEunhasuR',
                            fontSize: '16px'
                        }}
                    />
                    <button
                        onClick={checkAnswer}
                        style={{
                            height: '40px',  // 높이 맞춤
                            padding: '0 20px',
                            backgroundColor: isDarkMode ? '#3a3b3c' : '#fff',
                            color: isDarkMode ? '#e4e6eb' : '#000',
                            border: isDarkMode ? '2px solid #a9c6f8' : '1px solid #ddd',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            transition: 'all 0.3s ease',
                            fontFamily: 'TTHakgyoansimEunhasuR',
                            fontSize: '16px',
                            whiteSpace: 'nowrap'  // 텍스트 줄바꿈 방지
                        }}
                        disabled={isQuizEnd || isLoading || isButtonDisabled}
                    >
                        제출
                    </button>
                </section>
                {result && <p style={{margin: '20px 0'}}>{result}</p>}
            </main>

            <footer style={{
                display: 'flex',
                justifyContent: 'space-around',
                alignItems: 'center',
                padding: '10px 20px',
                height: '15%'
            }}>
            </footer>
        </div>
    );
};
export default FillInTheBlank;
