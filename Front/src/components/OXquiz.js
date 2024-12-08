import React, {useState, useEffect} from 'react';
import {useParams} from 'react-router-dom';

function randomInt(min, max, count) {// Step 1: count/2만큼 중복되지 않는 랜덤 정수 추출 (fixedNums)
    // 1. 기본 배열 생성
    const array = Array.from({ length: count }, (_, i) => i);

    // 2. fixedArray 생성 (중복되지 않는 랜덤 정수 count/2개)
    const fixedCount = Math.floor(count / 2); // count의 절반만큼 고정할 숫자 개수
    const fixedArray = [];

    // 중복되지 않는 랜덤 숫자 fixedCount 개만큼 고르기
    while (fixedArray.length < fixedCount) {
        const randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
        if (!fixedArray.includes(randomNum)) {
            fixedArray.push(randomNum);
        }
    }

    // 3. 배열에 랜덤 값 할당
    const resultArray = array.map((item) => {
        // fixedArray에 포함되지 않은 인덱스는 랜덤값으로 변경
        if (!fixedArray.includes(item)) {
            return Math.floor(Math.random() * (max - min + 1)) + min;
        }
        return item; // fixedArray에 포함되면 해당 값 유지
    });

    return resultArray;
}

const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

const OXQuiz = ({ isDarkMode, vocabId, fetchVocabData, fetchJson, words, userId }) => {
    const [totalQuestions, setTotalQuestions] = useState(0);
    const [currentQuestion, setCurrentQuestion] = useState(0);
    const [wrongCount, setWrongCount] = useState(0);
    const [corCount, setCorCount] = useState(0);
    const [result, setResult] = useState('');
    const [wordsForQuiz, setWordsForQuiz] = useState([]); // 퀴즈에 쓰일 단어 목록
    const [isQuizEnd, setIsQuizEnd] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [isButtonDisabled, setIsButtonDisabled] = useState(false);

    const fetchData = async () => {
        await fetchVocabData(userId);

        const wordsWithRandomDefs = [];
        const randNums = randomInt(0, words.length - 1, words.length);
        words.forEach((word, index, array) => {
            const randWordNum = randNums[index];
            let randDefNum = randomInt(0, array[randWordNum].defs.length - 1, 1)[0];
            const def = array[randWordNum].defs[randDefNum].definition;
            const tuple = {expression: word.expression, definition: def};
            wordsWithRandomDefs.push(tuple);
        });
        setWordsForQuiz(wordsWithRandomDefs);
        console.log("wordsWithRandomDefs", wordsWithRandomDefs);
    };

    useEffect(() => {
        if(words.length === 0){
            setResult("단어가 없습니다")
            setIsQuizEnd(true);
        }
    }, [words, wordsForQuiz]);

    useEffect(() => {
        setIsLoading(true);
        setCurrentQuestion(0);
        setWrongCount(0);
        setCorCount(0);
        setResult('');
        setWordsForQuiz([]);
        setIsQuizEnd(false);
        setIsLoading(true);
        setIsButtonDisabled(false);
        fetchData();
        setTotalQuestions(words.length);
        setCurrentQuestion(0);
        setIsLoading(false);
    }, [vocabId]);

    const checkAnswer = async (input) => {
        setIsButtonDisabled(true);

        const isCorrect = (input) => {
            const result = words[currentQuestion].defs.find(
                (def) => def.definition === wordsForQuiz[currentQuestion].definition
            );

            if (input === true) {
                return result !== undefined;  // result가 있으면 true, 없으면 false
            }

            return result === undefined;  // result가 없으면 true, 있으면 false
        }

        let correct = corCount;
        let wrong = wrongCount;

        if (isCorrect(input)) {
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
            setResult(`맞힌 횟수: ${correct} / 틀린 횟수: ${wrong}`);
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
            height: '70vh',
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
                        backgroundColor: 'white',
                        border: '1px solid #ccc',
                        width: '60%',
                        marginBottom: '20px',
                        textAlign: 'center',
                        padding: '20px',
                        borderRadius: '8px'
                    }}>
                        <p>단어: {wordsForQuiz[currentQuestion]?.expression || '불러오는 중...'}</p>
                        <p>뜻: {wordsForQuiz[currentQuestion]?.definition || '불러오는 중...'}</p>
                        <p style={{display: result ? 'block' : 'none', margin: '20px 0'}}>
                        </p>
                    </section>
                ) : (
                    "로딩 중..."
                )}
                <p>{result}</p>
                <div style={{display: 'flex', gap: '10px'}}>
                    <button onClick={() => checkAnswer(true)}
                            style={{borderRadius: '50px', width: '100px', height: '100px'}}
                            disabled={isQuizEnd || isLoading || isButtonDisabled}>O
                    </button>
                    <button onClick={() => checkAnswer(false)}
                            style={{borderRadius: '50px', width: '100px', height: '100px'}}
                            disabled={isQuizEnd || isLoading || isButtonDisabled}>X
                    </button>
                </div>
            </main>
        </div>
    );
};

export default OXQuiz;
